/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.IOException;
import java.net.*;

import javax.net.SocketFactory;

/**
 * A socket factory for testing that tracks whether it's being used.
 * <p>
 *
 * An instance of this factory can be set as the value of the
 * <code>mail.&lt;protocol&gt;.socketFactory</code> property.
 *
 * @since	JavaMail 1.5.3
 * @author	Stephan Sann
 * @author	Bill Shannon
 */
public class TestSocketFactory extends SocketFactory {

    /** Holds a SocketFactory to pass all API-method-calls to */
    private SocketFactory defaultFactory = null;

    /** Was a socket created? */
    private boolean socketCreated;

    /**
     * Initializes a new TestSocketFactory.
     */
    public TestSocketFactory() {
	// Get the default SocketFactory to delegate all API-calls to.
	defaultFactory = SocketFactory.getDefault();
    }

    /**
     * Was a socket created using one of the createSocket methods?
     */
    public boolean getSocketCreated() {
	return socketCreated;
    }


    // SocketFactory methods

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket()
     */
    @Override
    public synchronized Socket createSocket() throws IOException {
	Socket socket = defaultFactory.createSocket();
	socketCreated = true;
	return socket;
    }

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int,
     *						java.net.InetAddress, int)
     */
    @Override
    public synchronized Socket createSocket(InetAddress inetaddress, int i,
			InetAddress inetaddress1, int j) throws IOException {
	Socket socket =
		defaultFactory.createSocket(inetaddress, i, inetaddress1, j);
	socketCreated = true;
	return socket;
    }

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
     */
    @Override
    public synchronized Socket createSocket(InetAddress inetaddress, int i)
				throws IOException {
	Socket socket = defaultFactory.createSocket(inetaddress, i);
	socketCreated = true;
	return socket;
    }

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket(java.lang.String, int,
     *						java.net.InetAddress, int)
     */
    @Override
    public synchronized Socket createSocket(String s, int i,
				InetAddress inetaddress, int j)
				throws IOException, UnknownHostException {
	Socket socket = defaultFactory.createSocket(s, i, inetaddress, j);
	socketCreated = true;
	return socket;
    }

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket(java.lang.String, int)
     */
    @Override
    public synchronized Socket createSocket(String s, int i)
				throws IOException, UnknownHostException {
	Socket socket = defaultFactory.createSocket(s, i);
	socketCreated = true;
	return socket;
    }
}
