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

import com.sun.mail.imap.ReferralException;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

/**
 * Test IMAP login referrals (RFC 2221).
 */
public final class IMAPLoginReferralTest {

    private static final String REFERRAL_URL = "imap://test@server/";
    private static final String REFERRAL_MSG = "try server";
    private static final String REFERRAL =
			    "[REFERRAL " + REFERRAL_URL + "] " + REFERRAL_MSG;

    private static final int TIMEOUT = 1000;	// 1 second

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.millis(5 * TIMEOUT);

    /**
     * Test referral in BYE when connecting.
     */
    @Test
    public void testConnectReferral() {
	test(new IMAPHandler() {
			@Override
			public void sendGreetings() throws IOException {
			    bye(REFERRAL);
			}
		    });
    }

    /**
     * Test referral in NO response to LOGIN.
     */
    @Test
    public void testLoginReferral() {
	test(new IMAPHandler() {
			{{ capabilities += " LOGIN-REFERRALS"; }}
			@Override
			public void login() throws IOException {
			    no(REFERRAL);
			}
		    });
    }

    /**
     * Test referral in OK response to LOGIN.
     */
    @Test
    public void testLoginOkReferral() {
	test(new IMAPHandler() {
			{{ capabilities += " LOGIN-REFERRALS"; }}
			@Override
			public void login() throws IOException {
			    ok(REFERRAL);
			}
		    });
    }

    /**
     * Test referral in NO response to AUTHENTICATE PLAIN.
     */
    @Test
    public void testPlainReferral() {
	test(new IMAPHandler() {
			{{ capabilities += " LOGIN-REFERRALS AUTH=PLAIN"; }}
			@Override
			public void authplain(String ir) throws IOException {
			    no(REFERRAL);
			}
		    });
    }

    /**
     * Test referral in OK response to AUTHENTICATE PLAIN.
     */
    @Test
    public void testPlainOkReferral() {
	test(new IMAPHandler() {
			{{ capabilities += " LOGIN-REFERRALS AUTH=PLAIN"; }}
			@Override
			public void authplain(String ir) throws IOException {
			    if (ir == null) {
				cont("");
				String resp = readLine();
			    }
			    ok(REFERRAL);
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
	    properties.setProperty("mail.imap.referralexception", "true");
	    final Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    final IMAPStore store = (IMAPStore)session.getStore("imap");
	    try {
		store.connect("test", "test");
		fail("connect succeeded");
	    } catch (ReferralException ex) {
		// success!
		assertEquals(ex.getUrl(), REFERRAL_URL);
		assertEquals(ex.getText(), REFERRAL_MSG);
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
