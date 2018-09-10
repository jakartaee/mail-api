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
 * Test alerts.
 */
public final class IMAPAlertTest {

    private volatile boolean gotAlert = false;

    @Test
    public void test() {
        TestServer server = null;
        try {
            final IMAPHandler handler = new IMAPHandlerAlert();
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.imap.host", "localhost");
            properties.setProperty("mail.imap.port", "" + server.getPort());
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);
	    final CountDownLatch latch = new CountDownLatch(1);

            final Store store = session.getStore("imap");
	    store.addStoreListener(new StoreListener() {
		@Override
		public void notification(StoreEvent e) {
		    String s;
		    if (e.getMessageType() == StoreEvent.ALERT) {
			s = "ALERT: ";
			gotAlert = true;
			latch.countDown();
		    } else
			s = "NOTICE: ";
		    //System.out.println(s + e.getMessage());
		}
	    });
            try {
                store.connect("test", "test");
		// time for event to be delivered
		latch.await(5, TimeUnit.SECONDS);
		assertTrue(gotAlert);

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
     * Custom handler.  Returns an alert message at login.
     */
    private static final class IMAPHandlerAlert extends IMAPHandler {
	@Override
        public void login() throws IOException {
	    untagged("OK [ALERT] account is over quota");
	    super.login();
        }
    }
}
