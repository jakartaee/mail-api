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

package com.sun.mail.imap;

import java.io.*;
import java.util.Properties;
import java.net.ServerSocket;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.MessagingException;

import com.sun.mail.iap.ConnectionException;

import com.sun.mail.test.TestServer;
import com.sun.mail.util.MailConnectException;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test connect failure.
 */
public final class IMAPConnectFailureTest {

    private static final String HOST = "localhost";
    private static final int CTO = 20;

    @Test
    public void testNoServer() {
	try {
	    // verify that port is not being used
	    ServerSocket ss = new ServerSocket(0);
	    int port = ss.getLocalPort();
	    ss.close();

	    Properties properties = new Properties();
            properties.setProperty("mail.imap.host", HOST);
            properties.setProperty("mail.imap.port", "" + port);
            properties.setProperty("mail.imap.connectiontimeout", "" + CTO);
	    Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    Store store = session.getStore("imap");
	    try {
		store.connect("test", "test");
		fail("Connected!");
		// failure!
	    } catch (MailConnectException mcex) {
		// success!
		assertEquals(HOST, mcex.getHost());
		assertEquals(port, mcex.getPort());
		assertEquals(CTO, mcex.getConnectionTimeout());
	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
	    } finally {
		if (store.isConnected())
		    store.close();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	}
    }

    /**
     * Test that a disconnect after issuing the CAPABILITY command
     * results in a ConnectionException.
     */
    @Test
    public void testCapabilityDisconnect() {
	TestServer server = null;
	try {
	    final IMAPHandler handler = new IMAPHandler() {
		@Override
		public void sendGreetings() throws IOException {
		    untagged("OK IMAPHandler");
		}

		@Override
		public void capability() throws IOException {
		    exit();
		}
	    };
	    server = new TestServer(handler);
	    server.start();

	    Properties properties = new Properties();
	    properties.setProperty("mail.imap.host", "localhost");
	    properties.setProperty("mail.imap.port", "" + server.getPort());
	    final Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    final Store store = session.getStore("imap");
	    try {
		store.connect("test", "test");
		fail("connect did not fail");
	    } catch (MessagingException mex) {
		// this is what we expect, now check that it was caused by
		// the right exception
		assertTrue(mex.getCause() instanceof ConnectionException);
	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
	    } finally {
		store.close();
	    }

	} catch (final Exception e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	} finally {
	    if (server != null) {
		server.quit();
	    }
	}
    }
}
