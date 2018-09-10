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

package com.sun.mail.util;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.BodyPart;
import com.sun.mail.test.AsciiStringInputStream;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 * Test the "mail.mime.contenttypehandler" property.
 */
public class ContentTypeCleaner {
 
    private static Session s = Session.getInstance(new Properties());

    @BeforeClass
    public static void before() {
	System.out.println("ContentTypeCleaner");
	System.setProperty("mail.mime.contenttypehandler",
	    ContentTypeCleaner.class.getName());
    }

    @Test
    public void testGarbage() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	BodyPart bp = mp.getBodyPart(0);
	assertEquals("text/plain", bp.getContentType());
	assertEquals("first part\n", bp.getContent());
    }

    @Test
    public void testValid() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	BodyPart bp = mp.getBodyPart(1);
	assertEquals("text/plain; charset=iso-8859-1", bp.getContentType());
	assertEquals("second part\n", bp.getContent());
    }

    @Test
    public void testEmpty() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	BodyPart bp = mp.getBodyPart(2);
	assertEquals("text/plain", bp.getContentType());
	assertEquals("third part\n", bp.getContent());
    }

    public static String cleanContentType(MimePart mp, String contentType) {
	if (contentType == null)
	    return null;
	if (contentType.equals("complete garbage"))
	    return "text/plain";
	return contentType;
    }

    private static MimeMessage createMessage() throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Subject: Example\n" +
	    "Content-Type: multipart/mixed; boundary=\"-\"\n" +
	    "\n" +
	    "preamble\n" +
	    "---\n" +
	    "Content-Type: complete garbage\n" +
	    "\n" +
	    "first part\n" +
	    "\n" +
	    "---\n" +
	    "Content-Type: text/plain; charset=iso-8859-1\n" +
	    "\n" +
	    "second part\n" +
	    "\n" +
	    "---\n" +
	    "\n" +
	    "third part\n" +
	    "\n" +
	    "-----\n";

	return new MimeMessage(s, new AsciiStringInputStream(content));
    }
}
