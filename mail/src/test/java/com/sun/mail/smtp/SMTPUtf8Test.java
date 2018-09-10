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
import java.util.StringTokenizer;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.AuthenticationFailedException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test UTF-8 user names and recipients.
 */
public class SMTPUtf8Test {

    /**
     * Test using UTF-8 user name.
     */
    @Test
    public void testUtf8UserName() {
        TestServer server = null;
	final String user = "test\u03b1";
        try {
            server = new TestServer(new SMTPLoginHandler() {
		@Override
		public void auth(String line) throws IOException {
		    username = user;
		    password = user;
		    super.auth(line);
		}
	    });
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.auth.mechanisms", "LOGIN");
	    properties.setProperty("mail.mime.allowutf8",  "true");
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect(user, user);
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
     * Test using UTF-8 user name but without mail.mime.allowutf8.
     */
    @Test
    public void testUtf8UserNameNoAllowUtf8() {
        TestServer server = null;
	final String user = "test\u03b1";
        try {
            server = new TestServer(new SMTPLoginHandler() {
		@Override
		public void auth(String line) throws IOException {
		    username = user;
		    password = user;
		    super.auth(line);
		}
	    });
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.auth.mechanisms", "LOGIN");
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect(user, user);
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
     * Test using UTF-8 user name and PLAIN but without mail.mime.allowutf8.
     */
    @Test
    public void testUtf8UserNamePlain() {
        TestServer server = null;
	final String user = "test\u03b1";
        try {
            server = new TestServer(new SMTPLoginHandler() {
		@Override
		public void auth(String line) throws IOException {
		    username = user;
		    password = user;
		    super.auth(line);
		}
	    });
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.auth.mechanisms", "PLAIN");
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect(user, user);
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

    private static class Envelope {
	public String from;
	public String to;
    }

    /**
     * Test using UTF-8 From and To address.
     */
    @Test
    public void testUtf8From() {
        TestServer server = null;
	final String test = "test\u03b1";
	final String saddr = test + "@" + test + ".com";
	final Envelope env = new Envelope();
        try {
            server = new TestServer(new SMTPHandler() {
		@Override
		public void ehlo() throws IOException {
		    println("250-hello");
		    println("250-SMTPUTF8");
		    println("250 AUTH PLAIN");
		}

		@Override
		public void mail(String line) throws IOException {
		    StringTokenizer st = new StringTokenizer(line);
		    st.nextToken();	// skip "MAIL"
		    env.from = st.nextToken().
				    replaceFirst("FROM:<(.*)>", "$1");
		    if (!st.hasMoreTokens() ||
			    !st.nextToken().equals("SMTPUTF8"))
			println("500 fail");
		    else
			ok();
		}

		@Override
		public void rcpt(String line) throws IOException {
		    StringTokenizer st = new StringTokenizer(line);
		    st.nextToken();	// skip "RCPT"
		    env.to = st.nextToken().
				    replaceFirst("TO:<(.*)>", "$1");
		    ok();
		}
	    });
            server.start();

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
	    properties.setProperty("mail.mime.allowutf8",  "true");
            //properties.setProperty("mail.debug.auth", "true");
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
		MimeMessage msg = new MimeMessage(session);
		InternetAddress addr = new InternetAddress(saddr, test);
		msg.setFrom(addr);
		msg.setRecipient(Message.RecipientType.TO, addr);
		msg.setSubject(test);
		msg.setText(test + "\n");
                t.connect("test", "test");
		t.sendMessage(msg, msg.getAllRecipients());
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
	// after we're sure the server is done
	assertEquals(saddr, env.from);
	assertEquals(saddr, env.to);
    }
}
