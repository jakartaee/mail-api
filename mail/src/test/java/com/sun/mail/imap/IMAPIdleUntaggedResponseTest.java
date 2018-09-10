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

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.FetchProfile;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test untagged responses before IDLE continuation.
 */
public final class IMAPIdleUntaggedResponseTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    @Test
    public void test() {
        TestServer server = null;
        try {
            final IMAPHandlerIdleExists handler = new IMAPHandlerIdleExists();
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.imap.host", "localhost");
            properties.setProperty("mail.imap.port", "" + server.getPort());
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Store store = session.getStore("imap");
	    Folder folder0 = null;
            try {
                store.connect("test", "test");
                final Folder folder = store.getFolder("INBOX");
		folder0 = folder;
                folder.open(Folder.READ_ONLY);

		// create a thread to make sure we're kicked out of idle
		Thread t = new Thread() {
		    @Override
		    public void run() {
			try {
			    handler.waitForIdle();
			    // now do something that is sure to touch the server
			    FetchProfile fp = new FetchProfile();
			    fp.add(FetchProfile.Item.ENVELOPE);
			    folder.fetch(folder.getMessages(), fp);
			} catch (Exception ex) {
			}
		    }
		};
		t.start();

		((com.sun.mail.imap.IMAPFolder)folder).idle();

		assertEquals("message count", 1, folder.getMessageCount());

	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
            } finally {
		if (folder0 != null)
		    folder0.close(false);
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
     * Custom handler.  Returns untagged responses before continuation,
     * followed by a flag change for one of the new messages, to make
     * sure the notification of the new message is seen.
     */
    private static final class IMAPHandlerIdleExists extends IMAPHandler {
	// must be static because handler is cloned for each connection
	private static CountDownLatch latch = new CountDownLatch(1);

	@Override
        public void examine(String line) throws IOException {
	    numberOfMessages = 1;
	    super.examine(line);
	}

	@Override
        public void idle() throws IOException {
            untagged("1 EXISTS");
            untagged("1 RECENT");
	    cont();
            untagged("1 FETCH (FLAGS (\\Recent \\Seen))");
	    latch.countDown();
	    idleWait();
	    ok();
        }

	public void waitForIdle() throws InterruptedException {
	    latch.await();
	}
    }
}
