/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
import java.net.Socket;
import java.net.InetAddress;
import javax.net.SocketFactory;

/**
 * A SocketFactory that saves the Socket it creates so that it can be
 * accessed later.  Useful for checking that sockets are closed properly.
 */
public class SavedSocketFactory extends SocketFactory {
    private SocketFactory factory;
    private Socket saved;

    public SavedSocketFactory() {
	super();
	try {
	    factory = SocketFactory.getDefault();
	} catch(Exception ex) {
	    // ignore
	}
    }

    @Override
    public Socket createSocket() throws IOException {
	return save(factory.createSocket());
    }

    @Override
    public Socket createSocket(InetAddress host, int port)
				throws IOException {
	return save(factory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port,
				InetAddress localAddress, int localPort)
				throws IOException {
	return save(factory.createSocket(
				    address, port, localAddress, localPort));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
	return save(factory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port,
				InetAddress localHost, int localPort)
				throws IOException {
	return save(factory.createSocket(host, port, localHost, localPort));
    }

    public Socket getSocket() {
	return saved;
    }

    private Socket save(Socket s) {
	saved = s;
	return saved;
    }
}
