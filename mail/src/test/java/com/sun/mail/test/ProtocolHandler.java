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

package com.sun.mail.test;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.PushbackInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.net.ssl.SSLException;

/**
 * Handle protocol connection.
 *
 * Inspired by, and derived from, POP3Handler by sbo.
 *
 * @author sbo
 * @author Bill Shannon
 */
public abstract class ProtocolHandler implements Runnable, Cloneable {

    /** Logger for this class. */
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    /** Client socket. */
    protected Socket clientSocket;

    /** Quit? */
    protected boolean quit;

    /** Writer to socket. */
    protected PrintWriter writer;

    /** Input from socket. */
    protected InputStream in;

    /**
     * Sets the client socket.
     *
     * @param clientSocket	the client socket
     */
    public final void setClientSocket(final Socket clientSocket)
				throws IOException {
        this.clientSocket = clientSocket;
	writer = new PrintWriter(new OutputStreamWriter(
		    clientSocket.getOutputStream(), StandardCharsets.UTF_8));
	in = new BufferedInputStream(clientSocket.getInputStream());
    }

    /**
     * Optionally send a greeting when first connected.
     */
    public void sendGreetings() throws IOException {
    }

    /**
     * Read and process a single command.
     */
    public abstract void handleCommand() throws IOException;

    /**
     * Read a single line terminated by newline or CRLF.
     * Convert the UTF-8 bytes in the line (minus the line terminator)
     * to a String.
     */
    protected String readLine() throws IOException {
        byte[] buf = new byte[128];

        int room = buf.length;
        int offset = 0;
        int c;

	while ((c = in.read()) != -1) {
	    if (c == '\n') {
		break;
	    } else if (c == '\r') {
		int c2 = in.read();
		if ((c2 != '\n') && (c2 != -1)) {
		    if (!(in instanceof PushbackInputStream))
			this.in = new PushbackInputStream(in);
		    ((PushbackInputStream)in).unread(c2);
		}
		break;
	    } else {
		if (--room < 0) {
		    byte[] nbuf = new byte[offset + 128];
		    room = nbuf.length - offset - 1;
		    System.arraycopy(buf, 0, nbuf, 0, offset);
		    buf = nbuf;
		}
		buf[offset++] = (byte)c;
	    }
	}
	if ((c == -1) && (offset == 0))
	    return null;
	return new String(buf, 0, offset, StandardCharsets.UTF_8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void run() {
        try {

            sendGreetings();

            while (!quit) {
                handleCommand();
            }

            //clientSocket.close();
	} catch (SocketException sex) {
	    // ignore it, often get "connection reset" when client closes
	} catch (SSLException sex) {
	    // ignore it, often occurs when testing SSL
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error", e);
        } finally {
            try {
		if (clientSocket != null)
		    clientSocket.close();
            } catch (final IOException ioe) {
                LOGGER.log(Level.SEVERE, "Error", ioe);
            }
        }
    }

    /**
     * Quit.
     */
    public void exit() {
        quit = true;
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
		clientSocket = null;
            }
        } catch (final IOException e) {
            LOGGER.log(Level.SEVERE, "Error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
