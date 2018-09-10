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

import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test that capabilities are updated after login.
 */
public final class IMAPLoginCapabilitiesTest {

    private static final String NEWCAP = "NEWCAP";

    private static final int TIMEOUT = 1000;	// 1 second

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.millis(5 * TIMEOUT);

    /**
     * Test untagged CAPABILITY response after LOGIN.
     * This is illegal, but mail.ru and AOL do it.
     */
    @Test
    public void testUntaggedCapabilityAfterLogin() {
	test(new IMAPHandler() {
			@Override
			public void login() throws IOException {
			    untagged("CAPABILITY " + capabilities +
								" " + NEWCAP);
			    ok("LOGIN completed");
			}
		    });
    }

    /**
     * Test multiple untagged CAPABILITY responses after LOGIN.
     * This should NEVER happen, but we handle it just in case.
     */
    @Test
    public void testMultipleUntaggedCapabilityAfterLogin() {
	test(new IMAPHandler() {
			@Override
			public void login() throws IOException {
			    untagged("CAPABILITY " + capabilities);
			    untagged("CAPABILITY " + NEWCAP);
			    ok("LOGIN completed");
			}
		    });
    }

    /**
     * Test untagged CAPABILITY response after AUTHENTICATE.
     */
    @Test
    public void testUntaggedCapabilityAfterAuthenticate() {
	test(new IMAPHandler() {
			{{ capabilities += " AUTH=PLAIN"; }}
			@Override
			public void authplain(String ir) throws IOException {
			    untagged("CAPABILITY " + capabilities +
								" " + NEWCAP);
			    ok("AUTHENTICATE completed");
			}
		    });
    }

    private void test(IMAPHandler handler) {
	TestServer server = null;
	try {
	    server = new TestServer(handler);
	    server.start();

	    final Properties properties = new Properties();
	    properties.setProperty("mail.imap.host", "localhost");
	    properties.setProperty("mail.imap.port", "" + server.getPort());
	    //properties.setProperty("mail.debug.auth", "true");
	    final Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    final IMAPStore store = (IMAPStore)session.getStore("imap");
	    try {
		store.connect("test", "test");
		assertTrue(store.hasCapability(NEWCAP));
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
