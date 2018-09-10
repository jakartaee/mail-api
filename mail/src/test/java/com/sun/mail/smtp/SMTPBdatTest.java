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
import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

/**
 * Test the BDAT command.
 */
public final class SMTPBdatTest {

    private byte[] message;

    @Test
    public void testBdatSuccess() throws Exception {
        TestServer server = null;
        try {
	    SMTPHandler handler = new SMTPHandler() {
		{{ extensions.add("CHUNKING"); }}

		@Override
		public void setMessage(byte[] msg) {
		    message = msg;
		}
	    };
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.chunksize", "128");
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Transport t = session.getTransport("smtp");
            try {
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, "joe@example.com");
		msg.setSubject("test");
		msg.setText("test\r\n");
                t.connect();
		t.sendMessage(msg, msg.getAllRecipients());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		msg.writeTo(bos);
		bos.close();
		byte[] orig = bos.toByteArray();
		assertArrayEquals(orig, message);
	    } catch (MessagingException ex) {
		fail(ex.getMessage());
            } finally {
                t.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
		// wait for handler to exit
		server.join();
            }
        }
    }

    @Test
    public void testBdatFailure() throws Exception {
        TestServer server = null;
        try {
	    SMTPHandler handler = new SMTPHandler() {
		{{ extensions.add("CHUNKING"); }}

		@Override
		public void bdat(String line) throws IOException {
		    String[] tok = line.split("\\s+");
		    int bytes = Integer.parseInt(tok[1]);
		    boolean last = tok.length > 2 &&
				    tok[2].equalsIgnoreCase("LAST");
		    readBdatMessage(bytes, last);
		    println("444 failed");
		}
	    };
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.chunksize", "128");
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Transport t = session.getTransport("smtp");
            try {
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, "joe@example.com");
		msg.setSubject("test");
		msg.setText("test\r\n");
                t.connect();
		t.sendMessage(msg, msg.getAllRecipients());
		fail("no exception");
	    } catch (MessagingException ex) {
		// expect it to fail
            } finally {
                t.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
		// wait for handler to exit
		server.join();
            }
        }
    }
}
