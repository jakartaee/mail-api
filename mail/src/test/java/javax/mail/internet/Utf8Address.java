/*
 * Copyright (c) 2010, 2019 Oracle and/or its affiliates. All rights reserved.
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

package javax.mail.internet;

import com.sun.mail.test.AsciiStringInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.BodyPart;

import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 * Test "mail.mime.allowutf8" System property with address headers.
 */
public class Utf8Address {
 
    private static Session s = Session.getInstance(new Properties());
    private static String utf8name = "test\u00a1\u00a2\u00a3";

    @BeforeClass
    public static void before() {
	System.out.println("Utf8Address");
	s.getProperties().setProperty("mail.mime.allowutf8", "true");
    }

    @Test
    public void testFrom() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setFrom(new InternetAddress("joe@example.com", utf8name, "UTF-8"));
	m.saveChanges();
	String h = m.getHeader("From", "");
	assertTrue(h.contains(utf8name));
    }

    @Test
    public void testFromString() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setFrom(utf8name + " <joe@example.com>");
	m.saveChanges();
	String h = m.getHeader("From", "");
	assertTrue(h.contains(utf8name));
    }

    @Test
    public void testTo() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setRecipient(Message.RecipientType.TO,
	    new InternetAddress("joe@example.com", utf8name, "UTF-8"));
	m.saveChanges();
	String h = m.getHeader("To", "");
	assertTrue(h.contains(utf8name));
    }

    @Test
    public void testToString() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setRecipients(Message.RecipientType.TO,
	    utf8name + " <joe@example.com>");
	m.saveChanges();
	String h = m.getHeader("To", "");
	assertTrue(h.contains(utf8name));
    }

    @Test
    public void testSender() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setSender(new InternetAddress("joe@example.com", utf8name, "UTF-8"));
	m.saveChanges();
	String h = m.getHeader("Sender", "");
	assertTrue(h.contains(utf8name));
    }

    @AfterClass
    public static void after() {
	// should be unnecessary
	s.getProperties().remove("mail.mime.allowutf8");
    }
}
