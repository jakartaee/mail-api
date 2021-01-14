/*
 * Copyright (c) 2010, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail.internet;

import jakarta.mail.Session;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * the Bad Character Shift table index use inconsistency between
 * the 670th line and 823th line which leads to some problem in
 * non-ascii situation, give a test here
 *
 * @author dslztx
 */
public class MimeMultipartBCSIndexTest {

    private String EMLContent = "From: dslztx@gmail.com \n" +
            "To: dslztx <dslztx@gmail.com>\n" +
            "Subject: bcs index test \n" +
            "Date: Sat, 25 Aug 2018 08:35:14 +0800\n" +
            "Content-Type: multipart/alternative;\n" +
            "\tboundary=\"----=_000_6675�������=----\"\n" +
            "\n" +
            "This is a multi-part message in MIME format.\n" +
            "\n" +
            "------=_000_6675�������=----\n" +
            "Content-Type: text/plain;\n" +
            "\tcharset=\"utf-8\"\n" +
            "Content-Transfer-Encoding: base64\n" +
            "\n" +
            "aGVsbG8gd29ybGQ=\n" +
            "\n" +
            "------=_000_6675�������=----\n" +
            "Content-Type: text/html;\n" +
            "\tcharset=\"utf-8\"\n" +
            "Content-Transfer-Encoding: base64\n" +
            "\n" +
            "PGh0bWw+CjxoZWFkZXI+PHRpdGxlPlRoaXMgaXMgdGl0bGU8L3RpdGxlPjwvaGVhZGVyPgo8Ym9\n" +
            "keT4KSGVsbG8gd29ybGQKPC9ib2R5Pgo8L2h0bWw+\n" +
            "\n" +
            "------=_000_6675�������=------";

    @Test
    public void testBCSTableIndexInconsistency() {

        try {
            InputStream in = new ByteArrayInputStream(EMLContent.getBytes("ISO-8859-1"));

            Session session = Session.getDefaultInstance(new Properties());

            MimeMessage mimeMessage = new MimeMessage(session,
                    in);

            MimeMultipart topMultipart = (MimeMultipart) mimeMessage.getContent();

            Assert.assertTrue(topMultipart.getCount() == 2);
            Assert.assertTrue(topMultipart.getBodyPart(0).getContent().equals("hello world"));
            Assert.assertTrue(topMultipart.getBodyPart(1).getContent().equals("<html>\n" +
                    "<header><title>This is title</title></header>\n" +
                    "<body>\n" +
                    "Hello world\n" +
                    "</body>\n" +
                    "</html>"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
