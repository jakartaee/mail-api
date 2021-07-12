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

package com.sun.mail.util;

import com.sun.mail.imap.IMAPHandler;
import com.sun.mail.test.ProtocolHandler;
import com.sun.mail.test.TestSSLSocketFactory;
import com.sun.mail.test.TestServer;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.Timeout;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test SocketFetcher.
 */
public final class SocketFetcherTest {

    // timeout the test in case of deadlock
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(20);

    /**
     * Test connecting with proxy host and port.
     */
    @Test
    public void testProxyHostPort() {
	assertTrue("proxy host, port", testProxy("proxy", "localhost", "PPPP"));
    }

    /**
     * Test connecting with proxy host and port and user name and password.
     */
    @Test
    public void testProxyHostPortUserPassword() {
	assertTrue("proxy host, port, user, password",
	    testProxyUserPassword("proxy", "localhost", "PPPP", "user", "pwd"));
    }

    /**
     * Test connecting with proxy host:port.
     */
    @Test
    public void testProxyHostColonPort() {
	assertTrue("proxy host:port", testProxy("proxy", "localhost:PPPP", null));
    }

    /**
     * Test connecting with socks host and port.
     */
    @Test
    public void testSocksHostPort() {
	assertTrue("socks host, port", testProxy("socks", "localhost", "PPPP"));
    }

    /**
     * Test connecting with socks host:port.
     */
    @Test
    public void testSocksHostColonPort() {
	assertTrue("socks host:port", testProxy("socks", "localhost:PPPP", null));
    }

    /**
     * Test connecting with no proxy.
     */
    @Test
    public void testNoProxy() {
	assertFalse("no proxy", testProxy("none", "localhost", null));
    }

    @Test
    public void testSSLSocketFactoryHostnameVerifierAcceptsConnections() throws Exception {
        testSSLSocketFactoryHostnameVerifier(true);
    }
    /**
     * Test connecting (IMAP) with SSL using a custom hostname verifier which will
     * reject all connections.
     *
     * @throws Exception
     */
    @Test
    public void testSSLSocketFactoryHostnameVerifierRejectsConnections() throws Exception {
        testSSLSocketFactoryHostnameVerifier(false);
    }

    /**
     * Utility method for testing a custom {@link HostnameVerifier}.
     *
     * @param acceptConnections Whether the {@link HostnameVerifier} should accept or reject connections.
     * @throws Exception
     */
    private void testSSLSocketFactoryHostnameVerifier(boolean acceptConnections) throws Exception {
        final Properties properties = new Properties();
        properties.setProperty("mail.imap.host", "localhost");
        properties.setProperty("mail.imap.ssl.enable", "true");

        TestSSLSocketFactory sf = new TestSSLSocketFactory();
        properties.put("mail.imap.ssl.socketFactory", sf);

        // don't fall back to non-SSL
        properties.setProperty("mail.imap.socketFactory.fallback", "false");

        class CustomHostnameVerifier implements HostnameVerifier {
            private boolean used = false;

            @Override
            public boolean verify(String hostname, SSLSession session) {
                used = true;
                return acceptConnections;
            }

            public boolean hasBeenUsed() {
                return used;
            }
        }

        CustomHostnameVerifier hnv = new CustomHostnameVerifier();
        properties.put("mail.imap.ssl.hostnameverifier", hnv);
        properties.setProperty("mail.imap.ssl.checkserveridentity", "true"); // Required for hostname verification

        ThrowingRunnable runnable = new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                TestServer server = null;
                try {
                    server = new TestServer(new IMAPHandler(), true);
                    server.start();

                    properties.setProperty("mail.imap.port", "" + server.getPort());
                    final Session session = Session.getInstance(properties);

                    final Store store = session.getStore("imap");
                    store.connect("test", "test");
                }
                finally {
                    if (server != null) {
                        server.quit();
                    }
                }
            }
        };

        if (!acceptConnections) {
            // When the hostname verifier refuses a connection, a MessagingException will be thrown.
            assertThrows(MessagingException.class, runnable);
        }
        else {
            // When the hostname verifier is not set to refuse connections, no exception should be thrown.
            try {
                runnable.run();
            }
            catch (Throwable t) {
                fail("Unexpected exception thrown.");
            }
        }

        // Ensure the custom hostname verifier was actually used.
        assertTrue("Custom hostname verifier was not used.", hnv.hasBeenUsed());
    }

    /**
     */
    public boolean testProxy(String type, String host, String port) {
	return testProxyUserPassword(type, host, port, null, null);
    }

    /**
     */
    public boolean testProxyUserPassword(String type, String host, String port,
					String user, String pwd) {
	TestServer server = null;
	try {
	    ProxyHandler handler = new ProxyHandler(type.equals("proxy"));
	    server = new TestServer(handler);
	    server.start();
	    String sport = "" + server.getPort();

	    //System.setProperty("mail.socket.debug", "true");
	    Properties properties = new Properties();
	    properties.setProperty("mail.test.host", "localhost");
	    properties.setProperty("mail.test.port", "2");
	    properties.setProperty("mail.test." + type + ".host",
				    host.replace("PPPP", sport));
	    if (port != null)
		properties.setProperty("mail.test." + type + ".port",
				    port.replace("PPPP", sport));
	    if (user != null)
		properties.setProperty("mail.test." + type + ".user", user);
	    if (pwd != null)
		properties.setProperty("mail.test." + type + ".password", pwd);

	    Socket s = null;
	    try {
		s = SocketFetcher.getSocket("localhost", 2,
					    properties, "mail.test", false);
	    } catch (Exception ex) {
		// ignore failure, which is expected
		//System.out.println(ex);
		//ex.printStackTrace();
	    } finally {
		if (s != null)
		    s.close();
	    }
	    if (!handler.getConnected())
		return false;
	    if (user != null && pwd != null)
		return (user + ":" + pwd).equals(handler.getUserPassword());
	    else
		return true;

	} catch (final Exception e) {
	    //e.printStackTrace();
	    fail(e.getMessage());
	    return false;	// XXX - doesn't matter
	} finally {
	    if (server != null) {
		server.quit();
	    }
	}
    }

    /**
     * Custom handler.  Remember whether any data was sent
     * and save user/password string;
     */
    private static class ProxyHandler extends ProtocolHandler {
	private boolean http;

	// must be static because handler is cloned for each connection
	private static volatile boolean connected;
	private static volatile String userPassword;

	public ProxyHandler(boolean http) {
	    this.http = http;
	    connected = false;
	}

	@Override
	public void handleCommand() throws IOException {
	    if (!http) {
		int c = in.read();
		if (c >= 0) {
		    // any data means a real client connected
		    connected = true;
		}
		exit();
	    }

	    // else, http...
	    String line;
	    while ((line = readLine()) != null) {
		// any data means a real client connected
		connected = true;
		if (line.length() == 0)
		    break;
		if (line.startsWith("Proxy-Authorization:")) {
		    int i = line.indexOf("Basic ") + 6;
		    String up = line.substring(i);
		    userPassword = new String(BASE64DecoderStream.decode(
				    up.getBytes(StandardCharsets.US_ASCII)),
				    StandardCharsets.UTF_8);
		}
	    }
	    exit();
	}

	public boolean getConnected() {
	    return connected;
	}

	public String getUserPassword() {
	    return userPassword;
	}
    }
}
