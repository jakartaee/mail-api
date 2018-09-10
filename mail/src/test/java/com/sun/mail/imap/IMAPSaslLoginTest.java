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

import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Test login using a SASL mechanism.
 */
public final class IMAPSaslLoginTest {

    /**
     * Test that login using a SASL mechanism works.
     */
    @Test
    public void testSaslLogin() {
	TestServer server = null;
	try {
	    IMAPHandler handler = new IMAPSaslHandler();
	    server = new TestServer(handler);
	    server.start();

	    Properties properties = new Properties();
	    properties.setProperty("mail.imap.host", "localhost");
	    properties.setProperty("mail.imap.port", "" + server.getPort());
	    properties.setProperty("mail.imap.sasl.enable", "true");
	    properties.setProperty("mail.imap.sasl.mechanisms", "DIGEST-MD5");
	    Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    Store store = session.getStore("imap");
	    try {
		store.connect("test", "test");
		// success!
	    } catch (MessagingException mex) {
		fail("login failed");
	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
	    } finally {
		store.close();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	} finally {
	    if (server != null) {
		server.quit();
	    }
	}
    }
}
