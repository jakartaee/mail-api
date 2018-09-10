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
import java.util.concurrent.CountDownLatch;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.event.ConnectionAdapter;
import javax.mail.event.ConnectionEvent;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test that the connection is closed when an IOException is detected.
 */
public final class SMTPIOExceptionTest {

    // timeout the test in case of failure
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(5);

    private boolean closed = false;

    private static final int TIMEOUT = 200;	// I/O timeout, in millis

    @Test
    public void test() throws Exception {
        TestServer server = null;
	final CountDownLatch closedLatch = new CountDownLatch(1);
        try {
	    SMTPHandler handler = new SMTPHandler() {
		@Override
		public void rcpt(String line) throws IOException {
		    try {
			// delay long enough to cause timeout
			Thread.sleep(2 * TIMEOUT);
		    } catch (Exception ex) { }
		    super.rcpt(line);
		}
	    };
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.timeout", "" + TIMEOUT);
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Transport t = session.getTransport("smtp");
	    /*
	     * Use a listener to detect the connection being closed
	     * because if we called isConnected() and the connection
	     * wasn't already closed, it will issue a command that
	     * might detect that the connection was closed, even
	     * though it wasn't closed already.
	     */
	    t.addConnectionListener(new ConnectionAdapter() {
		@Override
		public void closed(ConnectionEvent e) {
		    closedLatch.countDown();
		}
	    });
            try {
		MimeMessage msg = new MimeMessage(session);
		msg.setRecipients(Message.RecipientType.TO, "joe@example.com");
		msg.setSubject("test");
		msg.setText("test");
                t.connect();
		t.sendMessage(msg, msg.getAllRecipients());
	    } catch (MessagingException ex) {
		// expect an exception from sendMessage
		closedLatch.await();	// wait for the listener to run
		// if we get here, the listener was called - SUCCESS
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
