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

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server that does nothing, but keeps track of
 * whether the client socket was closed.
 *
 * Inspired by, and derived from, POP3Server by sbo.
 *
 * @author Bill Shannon
 */
public final class NopServer extends Thread {

    /** Server socket. */
    private ServerSocket serverSocket;

    /** Keep on? */
    private volatile boolean keepOn;

    /** Did we get EOF on the client socket? */
    private volatile boolean gotEOF = false;

    /**
     * Nop server.
     */
    public NopServer() throws IOException {
	serverSocket = new ServerSocket(0);
    }

    /**
     * Return the port the server is listening on.
     */
    public int getPort() {
	return serverSocket.getLocalPort();
    }

    /**
     * Exit Nop server.
     */
    public void quit() {
        try {
            keepOn = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                serverSocket = null;
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eof() {
	return gotEOF;
    }

    @Override
    public void run() {
        try {
            keepOn = true;

            while (keepOn) {
                try {
                    final Socket clientSocket = serverSocket.accept();
		    /*
		     * Do nothing but consume any input and throw it away.
		     * When we see EOF, remember it.
		     */
		    InputStream is = clientSocket.getInputStream();
		    while (is.read() >= 0)
			;
		    gotEOF = true;
                } catch (final IOException e) {
                    //e.printStackTrace();
                }
            }
        } finally {
            quit();
        }
    }
}
