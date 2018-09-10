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

import com.sun.mail.test.AsciiStringInputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.BodyPart;

import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 * Test some of the ways you might modify a message that has been
 * read from an input stream.
 */
public class ModifyMessageTest {
 
    private static Session s = Session.getInstance(new Properties());

    @Test
    public void testAddHeader() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
        m.setHeader("a", "b");
        m.saveChanges();

	MimeMessage m2 = new MimeMessage(m);
	assertEquals("b", m2.getHeader("a", null));
    }

    @Test
    public void testChangeHeader() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
        m.setHeader("Subject", "test");
        m.saveChanges();

	MimeMessage m2 = new MimeMessage(m);
	assertEquals("test", m2.getHeader("Subject", null));
    }

    @Test
    public void testAddContent() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	MimeBodyPart mbp = new MimeBodyPart();
        mbp.setText("test");
	mp.addBodyPart(mbp);
        m.saveChanges();

	MimeMessage m2 = new MimeMessage(m);
	mp = (MimeMultipart)m2.getContent();
	BodyPart bp = mp.getBodyPart(2);
	assertEquals("test", bp.getContent());
	// make sure nothing else changed
	bp = mp.getBodyPart(0);
	assertEquals("first part\n", bp.getContent());
	bp = mp.getBodyPart(1);
	assertEquals("second part\n", bp.getContent());
    }

    @Test
    public void testChangeContent() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	BodyPart bp = mp.getBodyPart(0);
        bp.setText("test");
        m.saveChanges();

	MimeMessage m2 = new MimeMessage(m);
	mp = (MimeMultipart)m2.getContent();
	bp = mp.getBodyPart(0);
	assertEquals("test", bp.getContent());
    }

    @Test
    public void testChangeNestedContent() throws Exception {
        MimeMessage m = createNestedMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	mp = (MimeMultipart)mp.getBodyPart(0).getContent();
	BodyPart bp = mp.getBodyPart(0);
        bp.setText("test");
        m.saveChanges();

	MimeMessage m2 = new MimeMessage(m);
	mp = (MimeMultipart)m2.getContent();
	mp = (MimeMultipart)mp.getBodyPart(0).getContent();
	bp = mp.getBodyPart(0);
	assertEquals("test", bp.getContent());
	// make sure other content is not changed or re-encoded
	MimeBodyPart mbp = (MimeBodyPart)mp.getBodyPart(1);
	assertEquals("second part\n", mbp.getContent());
	assertEquals("quoted-printable", mbp.getEncoding());
	mbp = (MimeBodyPart)mp.getBodyPart(2);
	assertEquals("third part\n", mbp.getContent());
	assertEquals("base64", mbp.getEncoding());
    }

    private static MimeMessage createMessage() throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Subject: Example\n" +
	    "Content-Type: multipart/mixed; boundary=\"-\"\n" +
	    "\n" +
	    "preamble\n" +
	    "---\n" +
	    "\n" +
	    "first part\n" +
	    "\n" +
	    "---\n" +
	    "\n" +
	    "second part\n" +
	    "\n" +
	    "-----\n";

	return new MimeMessage(s, new AsciiStringInputStream(content));
    }

    private static MimeMessage createNestedMessage() throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Subject: Example\n" +
	    "Content-Type: multipart/mixed; boundary=\"-\"\n" +
	    "\n" +
	    "preamble\n" +
	    "---\n" +
	    "Content-Type: multipart/mixed; boundary=\"x\"\n" +
	    "\n" +
	    "--x\n" +
	    "\n" +
	    "first part\n" +
	    "\n" +
	    "--x\n" +
	    "Content-Transfer-Encoding: quoted-printable\n" +
	    "\n" +
	    "second part\n" +
	    "\n" +
	    "--x\n" +
	    "Content-Transfer-Encoding: base64\n" +
	    "\n" +
	    "dGhpcmQgcGFydAo=\n" +	// "third part\n", base64 encoded
	    "\n" +
	    "--x--\n" +
	    "-----\n";

	return new MimeMessage(s, new AsciiStringInputStream(content));
    }
}
