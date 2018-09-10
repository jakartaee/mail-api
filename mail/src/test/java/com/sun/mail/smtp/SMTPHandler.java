/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.mail.smtp;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.mail.test.ProtocolHandler;

/**
 * Handle connection.
 *
 * @author Bill Shannon
 */
public class SMTPHandler extends ProtocolHandler {

    /** Current line. */
    private String currentLine;

    /** A message being accumulated. */
    private ByteArrayOutputStream messageStream;

    /** SMTP extensions supported. */
    protected Set<String> extensions = new HashSet<String>();

    /**
     * Send greetings.
     *
     * @throws IOException
     *             unable to write to socket
     */
    @Override
    public void sendGreetings() throws IOException {
        println("220 localhost dummy server ready");
    }

    /**
     * Send String to socket.
     *
     * @param str
     *            String to send
     * @throws IOException
     *             unable to write to socket
     */
    public void println(final String str) throws IOException {
        writer.print(str);
	writer.print("\r\n");
        writer.flush();
    }

    /**
     * Handle command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    @Override
    public void handleCommand() throws IOException {
        currentLine = readLine();

        if (currentLine == null)
            return;

        final StringTokenizer st = new StringTokenizer(currentLine, " ");
        final String commandName = st.nextToken().toUpperCase();
        if (commandName == null) {
            LOGGER.severe("Command name is empty!");
            exit();
            return;
        }

        if (commandName.equals("HELO")) {
            helo();
        } else if (commandName.equals("EHLO")) {
            ehlo();
        } else if (commandName.equals("MAIL")) {
            mail(currentLine);
        } else if (commandName.equals("RCPT")) {
            rcpt(currentLine);
        } else if (commandName.equals("DATA")) {
            data();
        } else if (commandName.equals("BDAT")) {
            bdat(currentLine);
        } else if (commandName.equals("NOOP")) {
            noop();
        } else if (commandName.equals("RSET")) {
            rset();
        } else if (commandName.equals("QUIT")) {
            quit();
        } else if (commandName.equals("AUTH")) {
            auth(currentLine);
        } else {
            LOGGER.log(Level.SEVERE, "ERROR command unknown: {0}", commandName);
            println("-ERR unknown command");
        }
    }

    protected String readLine() throws IOException {
        currentLine = super.readLine();

        if (currentLine == null) {
	    // XXX - often happens when shutting down
            //LOGGER.severe("Current line is null!");
            exit();
        }
	return currentLine;
    }

    /**
     * HELO command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void helo() throws IOException {
        println("220 Ok");
    }

    /**
     * EHLO command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void ehlo() throws IOException {
        println("250-hello");
	for (String ext : extensions)
	    println("250-" + ext);
        println("250 AUTH PLAIN");	// PLAIN is simplest to fake
    }

    /**
     * MAIL command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void mail(String line) throws IOException {
	ok();
    }

    /**
     * RCPT command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void rcpt(String line) throws IOException {
	ok();
    }

    /**
     * DATA command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void data() throws IOException {
        println("354 go ahead");
	readMessage();
	ok();
    }

    /**
     * BDAT command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void bdat(String line) throws IOException {
        StringTokenizer st = new StringTokenizer(line, " ");
        String commandName = st.nextToken();
	int bytes = Integer.parseInt(st.nextToken());
	boolean last = st.hasMoreTokens() &&
			st.nextToken().equalsIgnoreCase("LAST");
	readBdatMessage(bytes, last);
	ok();
    }

    /**
     * Allow subclasses to override to save the message.
     */
    protected void setMessage(byte[] msg) {
    }

    /**
     * Consume the message and save it.
     */
    protected void readMessage() throws IOException {
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	PrintWriter pw = new PrintWriter(new OutputStreamWriter(bos, "utf-8"));
	String line;
	while ((line = super.readLine()) != null) {
	    if (line.equals("."))
		break;
	    if (line.startsWith("."))
		line = line.substring(1);
	    pw.print(line);
	    pw.print("\r\n");
	}
	pw.close();
	setMessage(bos.toByteArray());
    }

    /**
     * Consume a chunk of the message and save it.
     * Save the entire message when the last chunk is received.
     */
    protected void readBdatMessage(int bytes, boolean last) throws IOException {
	byte[] data = new byte[bytes];
	int len = data.length;
	int off = 0;
	int n;
	while (len > 0 && (n = in.read(data, off, len)) > 0) {
	    off += n;
	    len -= n;
	}
	if (messageStream == null)
	    messageStream = new ByteArrayOutputStream();
	messageStream.write(data);
	if (last) {
	    setMessage(messageStream.toByteArray());
	    messageStream = null;
	}
    }

    /**
     * NOOP command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void noop() throws IOException {
        ok();
    }

    /**
     * RSET command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void rset() throws IOException {
        ok();
    }

    /**
     * QUIT command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void quit() throws IOException {
        println("221 BYE");
        exit();
    }

    /**
     * AUTH command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void auth(String line) throws IOException {
        println("235 Authorized");
    }

    protected void ok() throws IOException {
	println("250 OK");
    }
}
