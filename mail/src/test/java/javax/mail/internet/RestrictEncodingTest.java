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
 * Test that the Content-Transfer-Encoding header is ignored
 * for composite parts.
 *
 * XXX - We don't test any of the properties that control this behavior.
 */
public class RestrictEncodingTest {
 
    private static Session s = Session.getInstance(new Properties());

    @Test
    public void testMultipart() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	assertEquals(2, mp.getCount());

	BodyPart bp = mp.getBodyPart(0);
	assertEquals("first part=\n", bp.getContent());
    }

    @Test
    public void testMessage() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();

	BodyPart bp = mp.getBodyPart(1);
	MimeMessage m2 = (MimeMessage)bp.getContent();
	assertEquals("message=\n", m2.getContent());
    }

    @Test
    public void testWrite() throws Exception {
        MimeMessage m = new MimeMessage(s);
	MimeMultipart mp = new MimeMultipart();
	MimeBodyPart mbp = new MimeBodyPart();
	mbp.setText("first part");
	mp.addBodyPart(mbp);
	MimeMessage m2 = new MimeMessage(s);
	m2.setSubject("example");
	m2.setText("message=\n");
	mbp = new MimeBodyPart();
	mbp.setContent(m2, "message/rfc822");
	mbp.setHeader("Content-Transfer-Encoding", "quoted-printable");
	mp.addBodyPart(mbp);
	m.setContent(mp);
	m.setHeader("Content-Transfer-Encoding", "quoted-printable");

	m = new MimeMessage(m);		// copy it
	mp = (MimeMultipart)m.getContent();

	BodyPart bp = mp.getBodyPart(1);
	m2 = (MimeMessage)bp.getContent();
	assertEquals("message=\n", m2.getContent());
    }

    private static MimeMessage createMessage() throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Content-Type: multipart/mixed; boundary=\"=3D\"\n" +
	    "Content-Transfer-Encoding: quoted-printable\n" +
	    "\n" +
	    "--=3D\n" +
	    "\n" +
	    "first part=\n" +
	    "\n" +
	    "--=3D\n" +
	    "Content-Type: message/rfc822\n" +
	    "Content-Transfer-Encoding: quoted-printable\n" +
	    "\n" +
	    "Subject: example\n" +
	    "\n" +
	    "message=\n" +
	    "\n" +
	    "--=3D--\n";

	return new MimeMessage(s, new AsciiStringInputStream(content));
    }
}
