/*
 * Copyright (c) 2009, 2019 Oracle and/or its affiliates. All rights reserved.
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
import java.util.Set;
import java.util.HashSet;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.FetchProfile;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test IMAP FetchProfile items.
 */
public final class IMAPFetchProfileTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    private static final String RDATE = "23-Jun-2004 06:26:26 -0700";
    private static final String ENVELOPE =
	"(\"Wed, 23 Jun 2004 18:56:42 +0530\" \"test\" " +
	"((\"Jakarta Mail\" NIL \"testuser\" \"example.com\")) " +
	"((\"Jakarta Mail\" NIL \"testuser\" \"example.com\")) " +
	"((\"Jakarta Mail\" NIL \"testuser\" \"example.com\")) " +
	"((NIL NIL \"testuser\" \"example.com\")) NIL NIL NIL " +
	"\"<40D98512.9040803@example.com>\")";

    public static interface IMAPTest {
	public void test(Folder folder, IMAPHandlerFetch handler)
				    throws MessagingException;
    }

    @Test
    public void testINTERNALDATEFetch() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerFetch handler)
				    throws MessagingException {
		    FetchProfile fp = new FetchProfile();
		    fp.add(IMAPFolder.FetchProfileItem.INTERNALDATE);
		    Message m = folder.getMessage(1);
		    folder.fetch(new Message[] { m }, fp);
		    assertTrue(handler.saw("INTERNALDATE"));
		    handler.reset();
		    assertTrue(m.getReceivedDate() != null);
		    assertFalse(handler.saw("INTERNALDATE"));
		}
	    },
	    new IMAPHandlerFetch() {
		@Override
		public void fetch(String line) throws IOException {
		    if (line.indexOf("INTERNALDATE") >= 0)
			saw.add("INTERNALDATE");
		    untagged("1 FETCH (INTERNALDATE \"" + RDATE + "\")");
		    ok();
		}
	    });
    }

    @Test
    public void testINTERNALDATEFetchEnvelope() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerFetch handler)
				    throws MessagingException {
		    FetchProfile fp = new FetchProfile();
		    fp.add(FetchProfile.Item.ENVELOPE);
		    Message m = folder.getMessage(1);
		    folder.fetch(new Message[] { m }, fp);
		    assertTrue(handler.saw("INTERNALDATE"));
		    handler.reset();
		    assertTrue(m.getReceivedDate() != null);
		    assertFalse(handler.saw("INTERNALDATE"));
		}
	    },
	    new IMAPHandlerFetch() {
		@Override
		public void fetch(String line) throws IOException {
		    if (line.indexOf("INTERNALDATE") >= 0)
			saw.add("INTERNALDATE");
		    untagged("1 FETCH (INTERNALDATE \"" + RDATE + "\")");
		    ok();
		}
	    });
    }

    @Test
    public void testINTERNALDATENoFetch() {
	testWithHandler(
	    new IMAPTest() {
		@Override
		public void test(Folder folder, IMAPHandlerFetch handler)
				    throws MessagingException {
		    Message m = folder.getMessage(1);
		    assertTrue(m.getReceivedDate() != null);
		    assertTrue(handler.saw("INTERNALDATE"));
		}
	    },
	    new IMAPHandlerFetch() {
		@Override
		public void fetch(String line) throws IOException {
		    if (line.indexOf("INTERNALDATE") >= 0)
			saw.add("INTERNALDATE");
		    untagged("1 FETCH (ENVELOPE " + ENVELOPE +
			" INTERNALDATE \"" + RDATE + "\" RFC822.SIZE 0)");
		    ok();
		}
	    });
    }

    public void testWithHandler(IMAPTest test, IMAPHandlerFetch handler) {
        TestServer server = null;
        try {
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.imap.host", "localhost");
            properties.setProperty("mail.imap.port", "" + server.getPort());
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Store store = session.getStore("imap");
	    Folder folder = null;
            try {
                store.connect("test", "test");
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_WRITE);
		test.test(folder, handler);
	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
            } finally {
		if (folder != null)
		    folder.close(false);
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
    private static class IMAPHandlerFetch extends IMAPHandler {
	// must be static because handler is cloned for each connection
	protected static Set<String> saw = new HashSet<>();

	@Override
        public void select(String line) throws IOException {
	    numberOfMessages = 1;
	    super.select(line);
	}

	public boolean saw(String item) {
	    return saw.contains(item);
	}

	public void reset() {
	    saw.clear();
	}
    }
}
