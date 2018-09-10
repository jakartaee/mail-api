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
import javax.mail.search.*;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test the search method.
 */
public final class IMAPSearchTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    @Test
    public void testWithinNotSupported() {
        TestServer server = null;
        try {
            server = new TestServer(new IMAPHandler() {
		@Override
		public void search(String line) throws IOException {
		    bad("WITHIN not supported");
		}
	    });
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.imap.host", "localhost");
            properties.setProperty("mail.imap.port", "" + server.getPort());
            properties.setProperty("mail.imap.throwsearchexception", "true");
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Store store = session.getStore("imap");
	    Folder folder = null;
            try {
                store.connect("test", "test");
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);
		Message[] msgs = folder.search(new YoungerTerm(1));
		fail("search didn't fail");
	    } catch (SearchException ex) {
		// success!
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
     * Test that when the server supports UTF8 and the client enables it,
     * the client doesn't issue a SEARCH CHARSET command even if the search
     * term includes a non-ASCII character.
     * (see RFC 6855, section 3, last paragraph)
     */
    @Test
    public void testUtf8Search() {
        TestServer server = null;
        try {
            server = new TestServer(new IMAPUtf8Handler() {
		@Override
		public void search(String line) throws IOException {
		    if (line.contains("CHARSET"))
			bad("CHARSET not supported");
		    else
			ok();
		}
	    });
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
		Message[] msgs = folder.search(new SubjectTerm("\u2019"));
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
     * An IMAPHandler that enables UTF-8 support.
     */
    private static class IMAPUtf8Handler extends IMAPHandler {
	{{ capabilities += " ENABLE UTF8=ACCEPT"; }}

	@Override
	public void enable(String line) throws IOException {
	    ok();
	}
    }
}
