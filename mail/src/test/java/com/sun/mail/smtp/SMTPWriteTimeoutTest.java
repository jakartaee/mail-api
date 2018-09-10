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

import javax.activation.DataHandler;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.fail;

/**
 * Test that write timeouts work.
 */
public final class SMTPWriteTimeoutTest {

    // timeout the test in case of failure
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(10);

    private static final int TIMEOUT = 200;	// write timeout, in millis

    @Test
    public void test() throws Exception {
        TestServer server = null;
        try {
	    SMTPHandler handler = new SMTPHandler() {
		@Override
		public void readMessage() throws IOException {
		    try {
			// delay long enough to cause timeout
			Thread.sleep(5 * TIMEOUT);
		    } catch (Exception ex) { }
		    super.readMessage();
		}
	    };
            server = new TestServer(handler);
            server.start();
            Thread.sleep(1000);

            final Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.writetimeout", "" + TIMEOUT);
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Transport t = session.getTransport("smtp");
            try {
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, "joe@example.com");
		msg.setSubject("test");
		byte[] bytes = new byte[16*1024*1024];
		msg.setDataHandler(
		    new DataHandler(new ByteArrayDataSource(bytes,
				    "application/octet-stream")));
                t.connect();
		t.sendMessage(msg, msg.getAllRecipients());
		fail("No exception");
	    } catch (MessagingException ex) {
		// expect an exception from sendMessage
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
		// wait long enough for handler to exit
		Thread.sleep(2 * TIMEOUT);
            }
        }
    }
}
