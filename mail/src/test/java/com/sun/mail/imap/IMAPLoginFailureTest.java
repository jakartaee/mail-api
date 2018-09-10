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

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.MessagingException;

import com.sun.mail.test.SavedSocketFactory;
import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test that login failures are handled correctly.
 */
public final class IMAPLoginFailureTest {

    /**
     * Test that login failures when no login methods are supported
     * cause the socket to be closed.
     */
    @Test
    public void testSocketClosed() {
	TestServer server = null;
	try {
	    final IMAPHandler handler = new IMAPHandler() {
		@Override
		public void sendGreetings() throws IOException {
		    capabilities = "IMAP4REV1 LOGINDISABLED";
		    super.sendGreetings();
		}
	    };
	    server = new TestServer(handler);
	    server.start();

	    SavedSocketFactory ssf = new SavedSocketFactory();
	    Properties properties = new Properties();
	    properties.setProperty("mail.imap.host", "localhost");
	    properties.setProperty("mail.imap.port", "" + server.getPort());
	    properties.put("mail.imap.socketFactory", ssf);
	    final Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    final Store store = session.getStore("imap");
	    try {
		store.connect("test", "test");
		fail("login did not fail");
	    } catch (MessagingException mex) {
		// this is what we expect, now check that the socket is closed
		assertTrue(ssf.getSocket().isClosed());
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
