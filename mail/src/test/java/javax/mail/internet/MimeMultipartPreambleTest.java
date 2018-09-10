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

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test the MIME multipart messages are parsed correctly and the
 * correct preamble is returned no matter what line terminators
 * are used.  This test found some bugs in the way LineInputStream
 * handled different line terminators.
 *
 * @author	trejkaz@kenai.com
 */
public class MimeMultipartPreambleTest {
    @SuppressWarnings({"SingleCharacterStringConcatenation"})
    private String THREE_PART_MAIL =
        "From: user1@example.com\n" +
        "To: user2@example.com\n" +
        "Subject: Receipts\n" +
        "Date: Wed, 14 Jul 2010 19:25:30 +1000\n" +
        "MIME-Version: 1.0\n" +
        "Content-Type: multipart/mixed;boundary=\"----=_NextPart_000_001C_01CB238A.52E35400\"\n" +
        "\n" +
        "This is a multi-part message in MIME format.\n" +
        "\n" +
        "------=_NextPart_000_001C_01CB238A.52E35400\n" +
        "Content-Type: text/plain;charset=\"us-ascii\"\n" +
        "Content-Transfer-Encoding: 7bit\n" +
        "\n" +
        "Hi.\n" +
        "\n" +
        "\n" +
        "------=_NextPart_000_001C_01CB238A.52E35400\n" +
        "Content-Type: application/pdf;name=\"Receipt 1.pdf\"\n" +
        "Content-Transfer-Encoding: base64\n" +
        "Content-Disposition: attachment;filename=\"Receipt 1.pdf\"\n" +
        "\n" +
        "JVBERi0xLjQKJcfsj6IKNSAwIG9iago8PC9MZW5ndGggNiAwIFIvRmlsdGVyIC9GbGF0ZURlY29k\n" +
        "\n" +
        "------=_NextPart_000_001C_01CB238A.52E35400\n" +
        "Content-Type: application/pdf;name=\"Receipt 2.pdf\"\n" +
        "Content-Transfer-Encoding: base64\n" +
        "Content-Disposition: attachment;filename=\"Receipt 2.pdf\"\n" +
        "\n" +
        "JVBERi0xLjQKJcfsj6IKNSAwIG9iago8PC9MZW5ndGggNiAwIFIvRmlsdGVyIC9GbGF0ZURlY29k\n" +
        "\n" +
        "------=_NextPart_000_001C_01CB238A.52E35400--\n" +
        "\n" +
        "\n";

    @Test
    public void testUnixLines() throws Exception {
        doThreePartMailTest(THREE_PART_MAIL);
    }

    @Test
    public void testWindowsLines() throws Exception {
        doThreePartMailTest(THREE_PART_MAIL.replace("\n", "\r\n"));
    }

    @Test
    public void testMacLines() throws Exception {
        doThreePartMailTest(THREE_PART_MAIL.replace('\n', '\r'));
    }

    /**
     * Performs a check that the multipart of the email was handled correctly.
     *
     * @param text the email text.
     * @throws Exception if an error occurs.
     */
    private void doThreePartMailTest(String text) throws Exception {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage mimeMessage = new MimeMessage(session,
			new ByteArrayInputStream(text.getBytes("US-ASCII")));

        MimeMultipart topMultipart = (MimeMultipart) mimeMessage.getContent();
        assertEquals("Wrong preamble",
	    "This is a multi-part message in MIME format.",
	    topMultipart.getPreamble().trim());
        assertEquals("Wrong number of parts", 3, topMultipart.getCount());

        BodyPart part1 = topMultipart.getBodyPart(0);
        assertEquals("Wrong content type for part 1",
	    "text/plain;charset=\"us-ascii\"", part1.getContentType());

        BodyPart part2 = topMultipart.getBodyPart(1);
        assertEquals("Wrong content type for part 2",
	    "application/pdf;name=\"Receipt 1.pdf\"", part2.getContentType());

        BodyPart part3 = topMultipart.getBodyPart(2);
        assertEquals("Wrong content type for part 3",
	    "application/pdf;name=\"Receipt 2.pdf\"", part3.getContentType());
    }
}
