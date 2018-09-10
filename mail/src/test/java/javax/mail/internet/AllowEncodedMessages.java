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
 * Test "mail.mime.allowencodedmessages" System property.
 */
public class AllowEncodedMessages {
 
    private static Session s = Session.getInstance(new Properties());

    @BeforeClass
    public static void before() {
	System.out.println("AllowEncodedMessages");
	System.setProperty("mail.mime.allowencodedmessages", "true");
    }

    @Test
    public void testEncodedMessages() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	BodyPart bp = mp.getBodyPart(0);
	assertEquals("message/rfc822", bp.getContentType());

	MimeMessage m2 = (MimeMessage)bp.getContent();
	assertEquals("text/plain", m2.getContentType());
	assertEquals("test message\r\n", m2.getContent());
    }

    @AfterClass
    public static void after() {
	// should be unnecessary
	System.clearProperty("mail.mime.allowencodedmessages");
    }

    private static MimeMessage createMessage() throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Subject: Example\n" +
	    "Content-Type: multipart/mixed; boundary=\"-\"\n" +
	    "\n" +
	    "---\n" +
	    "Content-Type: message/rfc822\n" +
	    "Content-Transfer-Encoding: base64\n" +
	    "\n" +
	    "TWltZS1WZXJzaW9uOiAxLjANClN1YmplY3Q6IH" +
	    "Rlc3QNCkNvbnRlbnQtVHlwZTogdGV4dC9wbGFp\n" +
	    "bg0KDQp0ZXN0IG1lc3NhZ2UNCg==\n" +
	    "\n" +
	    "-----\n";

	return new MimeMessage(s, new AsciiStringInputStream(content));
    }
}
