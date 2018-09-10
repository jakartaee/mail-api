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

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.StoreListener;
import javax.mail.event.StoreEvent;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test IMAP response events.
 */
public final class IMAPResponseEventTest {

    private volatile boolean gotResponse;

    /**
     * Test that response events are sent for the LOGIN command.
     */
    @Test
    public void testLoginResponseEvent() {
	testLogin("");
    }

    /**
     * Test that response events are sent for the AUTHENTICATE LOGIN command.
     */
    @Test
    public void testAuthLoginResponseEvent() {
	testLogin("LOGINDISABLED AUTH=LOGIN");
    }

    /**
     * Test that response events are sent for the AUTHENTICATE PLAIN command.
     */
    @Test
    public void testAuthPlainResponseEvent() {
	testLogin("LOGINDISABLED AUTH=PLAIN");
    }

    private void testLogin(String type) {
        TestServer server = null;
        try {
            final IMAPHandler handler = new IMAPHandlerLogin(type);
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.imap.host", "localhost");
            properties.setProperty("mail.imap.port", "" + server.getPort());
            properties.setProperty("mail.imap.enableresponseevents", "true");
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);
	    final CountDownLatch latch = new CountDownLatch(1);

            final Store store = session.getStore("imap");
	    store.addStoreListener(new StoreListener() {
		@Override
		public void notification(StoreEvent e) {
		    String s;
		    if (e.getMessageType() == IMAPStore.RESPONSE) {
			s = "RESPONSE: ";
			// is this the expected AUTHENTICATE response?
			if (e.getMessage().indexOf("X-LOGIN-SUCCESS") >= 0)
			    gotResponse = true;
			latch.countDown();
		    } else
			s = "OTHER: ";
		    //System.out.println(s + e.getMessage());
		}
	    });
	    gotResponse = false;
            try {
                store.connect("test", "test");
		// time for event to be delivered
		latch.await(5, TimeUnit.SECONDS);
		assertTrue(gotResponse);

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

    /**
     * Custom handler.  Forces use of specific login type and includes
     * a fake capability to be included in the OK response that we
     * will check for success.
     */
    private static final class IMAPHandlerLogin extends IMAPHandler {
	public IMAPHandlerLogin(String type) {
	    capabilities += " " + type + " X-LOGIN-SUCCESS";
	}
    }
}
