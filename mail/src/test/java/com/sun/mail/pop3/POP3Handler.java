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

package com.sun.mail.pop3;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.mail.test.ProtocolHandler;

/**
 * Handle connection.
 *
 * @author sbo
 */
public class POP3Handler extends ProtocolHandler {

    /** Current line. */
    private String currentLine;

    /** First test message. */
    private String top1 =
	    "Mime-Version: 1.0\r\n" +
	    "From: joe@example.com\r\n" +
	    "To: bob@example.com\r\n" +
	    "Subject: Example\r\n" +
	    "Content-Type: text/plain\r\n" +
	    "\r\n";
    private String msg1 = top1 +
	    "plain text\r\n";

    /** Second test message. */
    private String top2 =
	    "Mime-Version: 1.0\r\n" +
	    "From: joe@example.com\r\n" +
	    "To: bob@example.com\r\n" +
	    "Subject: Multipart Example\r\n" +
	    "Content-Type: multipart/mixed; boundary=\"xxx\"\r\n" +
	    "\r\n";
    private String msg2 = top2 +
	    "preamble\r\n" +
	    "--xxx\r\n" +
	    "\r\n" +
	    "first part\r\n" +
	    "\r\n" +
	    "--xxx\r\n" +
	    "\r\n" +
	    "second part\r\n" +
	    "\r\n" +
	    "--xxx--\r\n";

    /**
     * Send greetings.
     *
     * @throws IOException
     *             unable to write to socket
     */
    @Override
    public void sendGreetings() throws IOException {
        this.println("+OK POP3 CUSTOM");
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
        this.writer.print(str);
	this.writer.print("\r\n");
        this.writer.flush();
    }

    /**
     * Handle command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    @Override
    public void handleCommand() throws IOException {
        this.currentLine = readLine();

        if (this.currentLine == null) {
	    // probably just EOF because the socket was closed
            //LOGGER.severe("Current line is null!");
            this.exit();
            return;
        }

        final StringTokenizer st = new StringTokenizer(this.currentLine, " ");
        final String commandName = st.nextToken().toUpperCase();
        final String arg = st.hasMoreTokens() ? st.nextToken() : null;
        if (commandName == null) {
            LOGGER.severe("Command name is empty!");
            this.exit();
            return;
        }

        if (commandName.equals("STAT")) {
            this.stat();
        } else if (commandName.equals("LIST")) {
            this.list();
        } else if (commandName.equals("RETR")) {
            this.retr(arg);
        } else if (commandName.equals("DELE")) {
            this.dele();
        } else if (commandName.equals("NOOP")) {
            this.noop();
        } else if (commandName.equals("RSET")) {
            this.rset();
        } else if (commandName.equals("QUIT")) {
            this.quit();
        } else if (commandName.equals("TOP")) {
            this.top(arg);
        } else if (commandName.equals("UIDL")) {
            this.uidl();
        } else if (commandName.equals("USER")) {
            this.user();
        } else if (commandName.equals("PASS")) {
            this.pass();
        } else if (commandName.equals("CAPA")) {
            this.println("-ERR CAPA not supported");
        } else {
            LOGGER.log(Level.SEVERE, "ERROR command unknown: {0}", commandName);
            this.println("-ERR unknown command");
        }
    }

    /**
     * STAT command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void stat() throws IOException {
        this.println("+OK 2 " + (msg1.length() + msg2.length()));
    }

    /**
     * LIST command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void list() throws IOException {
        this.writer.println("+OK");
        this.writer.println("1 " + msg1.length());
        this.writer.println("2 " + msg2.length());
        this.println(".");
    }

    /**
     * RETR command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void retr(String arg) throws IOException {
	String msg;
	if (arg.equals("1"))
	    msg = msg1;
	else
	    msg = msg2;
        this.println("+OK " + msg.length() + " octets");
	this.writer.write(msg);
	this.println(".");
    }

    /**
     * DELE command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void dele() throws IOException {
	this.println("-ERR DELE not supported");
    }

    /**
     * NOOP command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void noop() throws IOException {
        this.println("+OK");
    }

    /**
     * RSET command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void rset() throws IOException {
        this.println("+OK");
    }

    /**
     * QUIT command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void quit() throws IOException {
        this.println("+OK");
        this.exit();
    }

    /**
     * TOP command.
     * XXX - ignores number of lines argument
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void top(String arg) throws IOException {
	String top;
	if (arg.equals("1"))
	    top = top1;
	else
	    top = top2;
        this.println("+OK " + top.length() + " octets");
	this.writer.write(top);
	this.println(".");
    }

    /**
     * UIDL command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void uidl() throws IOException {
        this.writer.println("+OK");
        this.writer.println("1 1");
        this.writer.println("2 2");
        this.println(".");
    }

    /**
     * USER command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void user() throws IOException {
        this.println("+OK");
    }

    /**
     * PASS command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    public void pass() throws IOException {
        this.println("+OK");
    }
}
