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

package com.sun.mail.pop3;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.MessagingException;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test that we can read POP3 messages.
 *
 * @author Bill Shannon
 */
public final class POP3MessageTest {

    private static TestServer server = null;
    private static Store store;
    private static Folder folder;

    private static void startServer(boolean cached) {
        try {
            final POP3Handler handler = new POP3Handler();
            server = new TestServer(handler);
            server.start();
            Thread.sleep(1000);

            final Properties properties = new Properties();
            properties.setProperty("mail.pop3.host", "localhost");
            properties.setProperty("mail.pop3.port", "" + server.getPort());
	    if (cached)
		properties.setProperty("mail.pop3.filecache.enable", "true");
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            store = session.getStore("pop3");
	    store.connect("test", "test");
	    folder = store.getFolder("INBOX");
	    folder.open(Folder.READ_ONLY);
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private static void stopServer() {
	try {
	    if (folder != null)
		folder.close(false);
	    if (store != null)
		store.close();
	} catch (MessagingException ex) {
	    // ignore it
	} finally {
	    if (server != null)
		server.quit();
	}
    }

    /**
     * Test that we can read the content of a message twice.
     * A bug caused POP3Message to return the same stream the
     * second time, instead of a new stream positioned at the
     * beginning of the data.  This caused multipart parsing
     * to fail.
     */
    @Test
    public void testReadTwice() throws Exception {
	readTwice(false);
    }

    /**
     * Now test it using the file cache.
     */
    @Test
    public void testReadTwiceCached() throws Exception {
	readTwice(true);
    }

    private void readTwice(boolean cached) throws Exception {
	startServer(cached);
	try {
	    Message[] msgs = folder.getMessages();
	    for (int i = 0; i < msgs.length; i++) {
		loadMail(msgs[i]);
		loadMail(msgs[i]);	
	    }
	} finally {
	    stopServer();
	}
	// no exception is success!
    }

    private void loadMail(Part p) throws Exception {
	Object content = p.getContent();
	if (content instanceof Multipart) {
	    Multipart mp = (Multipart)content;
	    int cnt = mp.getCount();
	    for (int i = 0; i < cnt; i++)
		loadMail(mp.getBodyPart(i));
	}
    }
}
