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

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.Flags;
import javax.mail.search.FlagTerm;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test FETCH and SEARCH responses with a message number that's out of range.
 */
public final class IMAPMessageNumberOutOfRangeTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    @Test
    public void test() {
        TestServer server = null;
        try {
            final IMAPHandlerBad handler = new IMAPHandlerBad();
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
                folder.open(Folder.READ_ONLY);
		Message msg = folder.getMessage(1);
		Flags f = msg.getFlags();
		Message[] msgs = folder.search(
			    new FlagTerm(new Flags(Flags.Flag.RECENT), true));
		assertEquals(1, msgs.length);
		assertEquals(msg, msgs[0]);
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
     * Custom handler.  Returns responses for messages that don't
     * exist in the folder.
     */
    private static final class IMAPHandlerBad extends IMAPHandler {

	@Override
        public void examine(String line) throws IOException {
	    numberOfMessages = 1;
	    numberOfRecentMessages = 1;
	    super.examine(line);
	}

	@Override
        public void search(String line) throws IOException {
            untagged("SEARCH 1 2");
	    ok();
        }

	@Override
        public void fetch(String line) throws IOException {
            untagged("1 FETCH (FLAGS (\\Recent))");
            untagged("2 FETCH (FLAGS (\\Deleted))");
	    ok();
        }
    }
}
