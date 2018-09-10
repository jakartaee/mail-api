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

import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.fail;

/**
 * Test that IMAP idle state is handled properly.
 */
public final class IMAPIdleStateTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    @Test
    public void test() {
        TestServer server = null;
        try {
            final IMAPHandlerIdleBye handler = new IMAPHandlerIdleBye();
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.imap.host", "localhost");
            properties.setProperty("mail.imap.port", "" + server.getPort());
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final IMAPStore store = (IMAPStore)session.getStore("imap");
            try {
                store.connect("test", "test");

		// create a thread to run the IDLE command on the Store
		Thread t = new Thread() {
		    @Override
		    public void run() {
			try {
			    store.idle();
			} catch (Exception ex) {
			}
		    }
		};
		t.start();
		handler.waitForIdle();

		// Now break it out of idle.
		// Need to use a method that doesn't check that the Store
		// is connected first.
		store.hasCapability("XXX");
		// no NullPointerException means the bug is fixed!

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
     * Custom handler.  Simulates the server sending a BYE response
     * to abort an IDLE.
     */
    private static final class IMAPHandlerIdleBye extends IMAPHandler {
	// must be static because handler is cloned for each connection
	private static CountDownLatch latch = new CountDownLatch(1);

	@Override
        public void idle() throws IOException {
	    cont();
	    latch.countDown();
	    // don't wait for DONE, just close the connection now
	    bye("closing");
        }

	public void waitForIdle() throws InterruptedException {
	    latch.await();
	}
    }
}
