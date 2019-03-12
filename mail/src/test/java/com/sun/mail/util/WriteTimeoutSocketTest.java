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

package com.sun.mail.util;

import java.lang.reflect.*;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.activation.DataHandler;
import javax.net.ssl.*;

import com.sun.mail.imap.IMAPHandler;
import com.sun.mail.test.TestServer;
import com.sun.mail.test.TestSocketFactory;
import com.sun.mail.test.TestSSLSocketFactory;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * Test that write timeouts work.
 */
public final class WriteTimeoutSocketTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    private static final int TIMEOUT = 200;	// ms
    private static final String data =
	"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Test write timeouts with plain sockets.
     */
    @Test
    public void test() {
	final Properties properties = new Properties();
	properties.setProperty("mail.imap.host", "localhost");
	properties.setProperty("mail.imap.writetimeout", "" + TIMEOUT);
	test(properties, false);
    }

    /**
     * Test write timeouts with custom socket factory.
     */
    @Test
    public void testSocketFactory() {
	final Properties properties = new Properties();
	properties.setProperty("mail.imap.host", "localhost");
	properties.setProperty("mail.imap.writetimeout", "" + TIMEOUT);
	TestSocketFactory sf = new TestSocketFactory();
	properties.put("mail.imap.socketFactory", sf);
	properties.setProperty("mail.imap.socketFactory.fallback", "false");
	test(properties, false);
	// make sure our socket factory was actually used
	assertTrue(sf.getSocketCreated());
    }

    /**
     * Test write timeouts with SSL sockets.
     */
    @Test
    public void testSSL() {
	final Properties properties = new Properties();
	properties.setProperty("mail.imap.host", "localhost");
	properties.setProperty("mail.imap.writetimeout", "" + TIMEOUT);
	properties.setProperty("mail.imap.ssl.enable", "true");
	properties.setProperty("mail.imap.ssl.trust", "localhost");
	test(properties, true);
    }

    /**
     * Test write timeouts with a custom SSL socket factory.
     */
    @Test
    public void testSSLSocketFactory() throws Exception {
	final Properties properties = new Properties();
	properties.setProperty("mail.imap.host", "localhost");
	properties.setProperty("mail.imap.writetimeout", "" + TIMEOUT);
	properties.setProperty("mail.imap.ssl.enable", "true");
	// TestSSLSocketFactory always trusts "localhost"; setting
	// this property would cause MailSSLSocketFactory to be used instead
	// of TestSSLSocketFactory, which we don't want.
	//properties.setProperty("mail.imap.ssl.trust", "localhost");
	TestSSLSocketFactory sf = new TestSSLSocketFactory();
	properties.put("mail.imap.ssl.socketFactory", sf);
	// don't fall back to non-SSL
	properties.setProperty("mail.imap.socketFactory.fallback", "false");
	test(properties, true);
	// make sure our socket factory was actually used
	assertTrue(sf.getSocketWrapped() || sf.getSocketCreated());
    }

    /**
     * Test that WriteTimeoutSocket overrides all methods from Socket.
     * XXX - this is kind of hacky since it depends on Method.toString
     */
    @Test
    public void testOverrides() throws Exception {
	Set<String> socketMethods = new HashSet<>();
	Method[] m = java.net.Socket.class.getDeclaredMethods();
	String className = java.net.Socket.class.getName() + ".";
	for (int i = 0; i < m.length; i++) {
	    if (Modifier.isPublic(m[i].getModifiers()) &&
		!Modifier.isStatic(m[i].getModifiers())) {
		String name = m[i].toString().
				    replace("synchronized ", "").
				    replace(className, "");
		socketMethods.add(name);
	    }
	}
	Set<String> wtsocketMethods = new HashSet<>();
	m = WriteTimeoutSocket.class.getDeclaredMethods();
	className = WriteTimeoutSocket.class.getName() + ".";
	for (int i = 0; i < m.length; i++) {
	    if (Modifier.isPublic(m[i].getModifiers())) {
		String name = m[i].toString().
				    replace("synchronized ", "").
				    replace(className, "");
		socketMethods.remove(name);
	    }
	}
	for (String s : socketMethods)
	    System.out.println("WriteTimeoutSocket did not override: " + s);
	assertTrue(socketMethods.isEmpty());
    }

    private void test(Properties properties, boolean isSSL) {
        TestServer server = null;
        try {
            final TimeoutHandler handler = new TimeoutHandler();
            server = new TestServer(handler, isSSL);
            server.start();

	    properties.setProperty("mail.imap.port", "" + server.getPort());
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

	    MimeMessage msg = new MimeMessage(session);
	    msg.setFrom("test@example.com");
	    msg.setSubject("test");
	    final int size = 8192000;	// enough data to fill network buffers
	    byte[] part = new byte[size];
	    for (int i = 0; i < size; i++) {
		int j = i % 64;
		if (j == 62)
		    part[i] = (byte)'\r';
		else if (j == 63)
		    part[i] = (byte)'\n';
		else
		    part[i] = (byte)data.charAt((j + i / 64) % 62);
	    }
	    msg.setDataHandler(new DataHandler(
		new ByteArrayDataSource(part, "text/plain")));
	    msg.saveChanges();

            final Store store = session.getStore("imap");
            try {
                store.connect("test", "test");
		final Folder f = store.getFolder("test");
		f.appendMessages(new Message[] { msg });
		fail("No timeout");
	    } catch (StoreClosedException scex) {
		// success!
	    } catch (Exception ex) {
		System.out.println(ex);
		//ex.printStackTrace();
		fail(ex.toString());
            } finally {
                store.close();
            }
        } catch (final Exception e) {
            //e.printStackTrace();
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
    private static final class TimeoutHandler extends IMAPHandler {
	@Override
        protected void collectMessage(int bytes) throws IOException {
	    try {
		// allow plenty of time for even slow machines to time out
		Thread.sleep(TIMEOUT*20);
	    } catch (InterruptedException ex) { }
	    super.collectMessage(bytes);
        }

	@Override
	public void list(String line) throws IOException {
	    untagged("LIST () \"/\" test");
	    ok();
	}
    }
}
