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

import java.io.*;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Test that failures while closing a folder are handled properly.
 */
public final class IMAPCloseFailureTest {

    private static final String HOST = "localhost";

    static class NoIMAPHandler extends IMAPHandler {
	static boolean first = true;

	@Override
	public void examine(String line) throws IOException {
	    if (first)
		no("mailbox gone");
	    else
		super.examine(line);
	    first = false;
	}
    }

    static class BadIMAPHandler extends IMAPHandler {
	static boolean first = true;

	@Override
	public void examine(String line) throws IOException {
	    if (first)
		bad("mailbox gone");
	    else
		super.examine(line);
	    first = false;
	}
    }

    @Test
    public void testCloseNo() {
	testClose(new NoIMAPHandler());
    }

    @Test
    public void testCloseBad() {
	testClose(new BadIMAPHandler());
    }

    public void testClose(IMAPHandler handler) {
	TestServer server = null;
	try {
	    server = new TestServer(handler);
	    server.start();

	    Properties properties = new Properties();
            properties.setProperty("mail.imap.host", HOST);
            properties.setProperty("mail.imap.port", "" + server.getPort());
	    Session session = Session.getInstance(properties);
	    //session.setDebug(true);

	    Store store = session.getStore("imap");
	    try {
		store.connect("test", "test");
		Folder f = store.getFolder("INBOX");
		f.open(Folder.READ_WRITE);
		f.close(false);
		// Make sure that failure while closing doesn't leave us
		// with a connection that can't be used to open a folder.
		f.open(Folder.READ_WRITE);
		f.close(false);
	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
	    } finally {
		if (store.isConnected())
		    store.close();
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	} finally {
	    if (server != null) {
		server.quit();
	    }
	}
    }
}
