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
import java.security.GeneralSecurityException;

import javax.net.ssl.*;

/**
 * An SSL socket factory for testing that tracks whether it's being used.
 * <p>
 *
 * An instance of this factory can be set as the value of the
 * <code>mail.&lt;protocol&gt;.ssl.socketFactory</code> property.
 *
 * @since	JavaMail 1.5.3
 * @author	Stephan Sann
 * @author	Bill Shannon
 */
public class TestSSLSocketFactory extends SSLSocketFactory {

    /** Holds a SSLSocketFactory to pass all API-method-calls to */
    private SSLSocketFactory defaultFactory = null;

    /** Was a socket created? */
    private boolean socketCreated;

    /** Was a socket wrapped? */
    private boolean socketWrapped;

    private String[] suites;

    /**
     * Initializes a new TestSSLSocketFactory.
     * 
     * @throws  GeneralSecurityException for security errors
     */
    public TestSSLSocketFactory() throws GeneralSecurityException {
	this("TLS");
    }

    /**
     * Initializes a new TestSSLSocketFactory with a given protocol.
     * Normally the protocol will be specified as "TLS".
     * 
     * @param   protocol  The protocol to use
     * @throws  NoSuchAlgorithmException if given protocol is not supported
     * @throws  GeneralSecurityException for security errors
     */
    public TestSSLSocketFactory(String protocol)
				throws GeneralSecurityException {

	// Get the default SSLSocketFactory to delegate all API-calls to.
	defaultFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    }

    /**
     * Was a socket created using one of the createSocket methods?
     */
    public boolean getSocketCreated() {
	return socketCreated;
    }

    /**
     * Was a socket wrapped using the createSocket method that takes a Socket?
     */
    public boolean getSocketWrapped() {
	return socketWrapped;
    }

    /**
     * Set the default cipher suites to be applied to future sockets.
     */
    public void setDefaultCipherSuites(String[] suites) {
	this.suites = suites;
    }

    /**
     * Configure the socket to be returned.
     */
    private Socket configure(Socket socket) {
	if (socket instanceof SSLSocket) {	// XXX - always true
	    SSLSocket s = (SSLSocket)socket;
	    if (suites != null)
		s.setEnabledCipherSuites(suites);
	}
	return socket;
    }


    // SocketFactory methods

    /* (non-Javadoc)
     * @see javax.net.ssl.SSLSocketFactory#createSocket(java.net.Socket,
     *						java.lang.String, int, boolean)
     */
    @Override
    public synchronized Socket createSocket(Socket socket, String s, int i,
				boolean flag) throws IOException {
	Socket wrappedSocket = defaultFactory.createSocket(socket, s, i, flag);
	socketWrapped = true;
	return configure(wrappedSocket);
    }

    /* (non-Javadoc)
     * @see javax.net.ssl.SSLSocketFactory#getDefaultCipherSuites()
     */
    @Override
    public synchronized String[] getDefaultCipherSuites() {
	if (suites != null)
	    return suites.clone();
	else
	    return defaultFactory.getDefaultCipherSuites();
    }

    /* (non-Javadoc)
     * @see javax.net.ssl.SSLSocketFactory#getSupportedCipherSuites()
     */
    @Override
    public synchronized String[] getSupportedCipherSuites() {
	return defaultFactory.getSupportedCipherSuites();
    }

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket()
     */
    @Override
    public synchronized Socket createSocket() throws IOException {
	Socket socket = defaultFactory.createSocket();
	socketCreated = true;
	return configure(socket);
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
	return configure(socket);
    }

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
     */
    @Override
    public synchronized Socket createSocket(InetAddress inetaddress, int i)
				throws IOException {
	Socket socket = defaultFactory.createSocket(inetaddress, i);
	socketCreated = true;
	return configure(socket);
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
	return configure(socket);
    }

    /* (non-Javadoc)
     * @see javax.net.SocketFactory#createSocket(java.lang.String, int)
     */
    @Override
    public synchronized Socket createSocket(String s, int i)
				throws IOException, UnknownHostException {
	Socket socket = defaultFactory.createSocket(s, i);
	socketCreated = true;
	return configure(socket);
    }
}
