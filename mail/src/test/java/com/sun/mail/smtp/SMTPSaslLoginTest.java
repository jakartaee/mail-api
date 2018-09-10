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

package com.sun.mail.smtp;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.AuthenticationFailedException;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test login using a SASL mechanism on the server
 * with SASL and non-SASL on the client.
 */
public class SMTPSaslLoginTest {

    /**
     * Test using non-SASL DIGEST-MD5.
     */
    @Test
    public void testSuccess() {
        TestServer server = null;
        try {
            server = new TestServer(new SMTPSaslHandler());
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect("test", "test");
		// success!
	    } catch (Exception ex) {
		fail(ex.toString());
            } finally {
                t.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
            }
        }
    }

    /**
     * Test using non-SASL DIGEST-MD5 with incorrect password.
     */
    @Test
    public void testFailure() {
        TestServer server = null;
        try {
            server = new TestServer(new SMTPSaslHandler());
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect("test", "xtest");
		// should have failed
		fail("wrong password succeeded");
	    } catch (AuthenticationFailedException ex) {
		// success!
	    } catch (Exception ex) {
		fail(ex.toString());
            } finally {
                t.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
            }
        }
    }

    /**
     * Test using SASL DIGEST-MD5.
     */
    @Test
    public void testSaslSuccess() {
        TestServer server = null;
        try {
            server = new TestServer(new SMTPSaslHandler());
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.sasl.enable", "true");
            properties.setProperty("mail.smtp.sasl.mechanisms", "DIGEST-MD5");
            properties.setProperty("mail.smtp.auth.digest-md5.disable", "true");
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect("test", "test");
		// success!
	    } catch (Exception ex) {
		fail(ex.toString());
            } finally {
                t.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
            }
        }
    }

    /**
     * Test using SASL DIGEST-MD5 with incorrect password.
     */
    @Test
    public void testSaslFailure() {
        TestServer server = null;
        try {
            server = new TestServer(new SMTPSaslHandler());
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.sasl.enable", "true");
            properties.setProperty("mail.smtp.sasl.mechanisms", "DIGEST-MD5");
            properties.setProperty("mail.smtp.auth.digest-md5.disable", "true");
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect("test", "xtest");
		// should have failed
		fail("wrong password succeeded");
	    } catch (AuthenticationFailedException ex) {
		// success!
	    } catch (Exception ex) {
		fail(ex.toString());
            } finally {
                t.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
            }
        }
    }

    /**
     * Test that AUTH with no mechanisms fails.
     */
    @Test
    public void testAuthNoParam() {
        TestServer server = null;
        try {
            server = new TestServer(new SMTPSaslHandler() {
		@Override
		public void ehlo() throws IOException {
		    println("250-hello");
		    println("250 AUTH");
		}
	    });
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect("test", "test");
		fail("Connect didn't fail");
	    } catch (AuthenticationFailedException ex) {
		// success
	    } catch (Exception ex) {
		fail(ex.toString());
            } finally {
                t.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
            }
        }
    }

    /**
     * Test that no AUTH succeeds by skipping authentication entirely.
     */
    @Test
    public void testNoAuth() {
        TestServer server = null;
        try {
            server = new TestServer(new SMTPSaslHandler() {
		@Override
		public void ehlo() throws IOException {
		    println("250-hello");
		    println("250 XXX");
		}
		@Override
		public void auth(String line) throws IOException {
		    println("501 Authentication failed");
		}
	    });
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect("test", "test");
		// success
	    } catch (Exception ex) {
		fail(ex.toString());
            } finally {
                t.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
            }
        }
    }
}
