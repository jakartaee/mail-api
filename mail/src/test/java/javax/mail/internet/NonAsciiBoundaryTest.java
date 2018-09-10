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
 * Test that non-ASCII boundary strings are handled reasonably,
 * even though, strictly speaking, the MIME spec doesn't allow them.
 */
public class NonAsciiBoundaryTest {
 
    private static Session s = Session.getInstance(new Properties());

    @Test
    public void test() throws Exception {
        MimeMessage m = createMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	assertEquals(2, mp.getCount());
	BodyPart bp = mp.getBodyPart(0);
	assertEquals("first part\n", bp.getContent());
	bp = mp.getBodyPart(1);
	assertEquals("second part\n", bp.getContent());
    }

    private static MimeMessage createMessage() throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Subject: Example\n" +
	    "Content-Type: multipart/mixed; boundary=\"\u00A9\"\n" +
	    "\n" +
	    "--\u00A9\n" +
	    "\n" +
	    "first part\n" +
	    "\n" +
	    "--\u00A9\n" +
	    "\n" +
	    "second part\n" +
	    "\n" +
	    "--\u00A9--\n";

	return new MimeMessage(s, new AsciiStringInputStream(content, false));
    }
}
