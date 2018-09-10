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
import java.util.StringTokenizer;
import java.util.Map;

import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test the IMAP ID command.
 */
public final class IMAPIDTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    @Test
    public void testIDNIL() {
        TestServer server = null;
        try {
            final IMAPHandler handler = new IMAPHandlerID();
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
		Map<String,String> id = store.id(null);
		assertEquals("true", id.get("test"));

	    } catch (Exception ex) {
		System.out.println(ex);
		ex.printStackTrace();
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
     * Custom handler.
     */
    private static final class IMAPHandlerID extends IMAPHandler {

	@Override
        public void id(String line) throws IOException {
	    StringTokenizer st = new StringTokenizer(line);
	    String tag = st.nextToken();
	    String cmd = st.nextToken();
	    String arg = st.nextToken();
	    untagged("ID (\"test\" \"" + arg.equals("NIL") + "\")");
	    ok();
	}
    }
}
