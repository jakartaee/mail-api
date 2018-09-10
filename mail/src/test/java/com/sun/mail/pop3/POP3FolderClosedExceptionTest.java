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
import javax.mail.FolderClosedException;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test that FolderClosedException is thrown when server times out connection.
 * This test is derived from real failures seen with Hotmail.
 *
 * @author sbo
 * @author Bill Shannon
 */
public final class POP3FolderClosedExceptionTest {

    /**
     * Test that FolderClosedException is thrown when the timeout occurs
     * when reading the message body.
     */
    @Test
    public void testFolderClosedExceptionBody() {
	TestServer server = null;
	try {
	    final POP3Handler handler = new POP3HandlerTimeoutBody();
	    server = new TestServer(handler);
	    server.start();
	    Thread.sleep(1000);

	    final Properties properties = new Properties();
	    properties.setProperty("mail.pop3.host", "localhost");
	    properties.setProperty("mail.pop3.port", "" + server.getPort());
	    final Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    final Store store = session.getStore("pop3");
	    try {
		store.connect("test", "test");
		final Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		Message msg = folder.getMessage(1);
		try {
		    msg.getContent();
		} catch (IOException ioex) {
		    // expected
		    // first attempt detects error return from server
		}
		// second attempt detects closed connection from server
		msg.getContent();

		// Check
		assertFalse(folder.isOpen());
	    } catch (FolderClosedException ex) {
		// success!
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
     * Custom handler.  Returns ERR for RETR the first time,
     * then closes the connection the second time.
     */
    private static final class POP3HandlerTimeoutBody extends POP3Handler {

	private boolean first = true;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void retr(String arg) throws IOException {
	    if (first) {
		println("-ERR Server timeout");
		first = false;
	    } else
		exit();
	}
    }

    /**
     * Test that FolderClosedException is thrown when the timeout occurs
     * when reading the headers.
     */
    @Test
    public void testFolderClosedExceptionHeaders() {
	TestServer server = null;
	try {
	    final POP3Handler handler = new POP3HandlerTimeoutHeader();
	    server = new TestServer(handler);
	    server.start();
	    Thread.sleep(1000);

	    final Properties properties = new Properties();
	    properties.setProperty("mail.pop3.host", "localhost");
	    properties.setProperty("mail.pop3.port", "" + server.getPort());
	    final Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    final Store store = session.getStore("pop3");
	    try {
		store.connect("test", "test");
		final Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		Message msg = folder.getMessage(1);
		msg.getSubject();

		// Check
		assertFalse(folder.isOpen());
	    } catch (FolderClosedException ex) {
		// success!
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
     * Custom handler.  Returns ERR for TOP, then closes connection.
     */
    private static final class POP3HandlerTimeoutHeader extends POP3Handler {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void top(String arg) throws IOException {
	    println("-ERR Server timeout");
	    exit();
	}
    }
}
