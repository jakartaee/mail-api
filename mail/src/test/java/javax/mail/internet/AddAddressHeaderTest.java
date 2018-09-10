/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Properties;

import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;

import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 * Test that "add" methods for address headers result in only a single
 * address header, per RFC 2822.
 */
public class AddAddressHeaderTest {
 
    private static Session s = Session.getInstance(new Properties());
    private static InternetAddress[] setList = new InternetAddress[1];
    private static InternetAddress[] addList = new InternetAddress[1];

    static {
	try {
	    setList[0] = new InternetAddress("me@example.com");
	    addList[0] = new InternetAddress("you@example.com");
	} catch (MessagingException ex) {
	}
    }

    @Test
    public void testFrom() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setFrom(setList[0]);
	m.addFrom(addList);
	m.saveChanges();
	String[] h = m.getHeader("From");
	assertEquals(1, h.length);
    }

    @Test
    public void testTo() throws Exception {
	testRecipients(Message.RecipientType.TO);
    }

    @Test
    public void testCc() throws Exception {
	testRecipients(Message.RecipientType.CC);
    }

    @Test
    public void testBcc() throws Exception {
	testRecipients(Message.RecipientType.BCC);
    }

    private void testRecipients(Message.RecipientType type) throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setRecipients(type, setList);
	m.addRecipients(type, addList);
	m.saveChanges();
	// XXX - depends on RecipientType.toString
	String[] h = m.getHeader(type.toString());
	assertEquals(1, h.length);
    }
}
