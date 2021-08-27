/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;

public class MimeBodyPartTest {

    @Test
    public void sessionProperties() throws MessagingException, IOException {
        try {
        Properties prop = new Properties();
        MimeMessage orig = buildFromProperties(prop);
        MimeMultipart omp = (MimeMultipart) orig.getContent();
        MimeBodyPart obp = (MimeBodyPart) omp.getBodyPart(0);
        // No properties added, so default will be checked
        // MimeMessage
        assertTrue(orig.setDefaultTextCharset);
        assertTrue(orig.setContentTypeFileName);
        assertFalse(orig.encodeFileName);
        assertFalse(orig.decodeFileName);
        assertTrue(orig.ignoreMultipartEncoding);
        assertTrue(orig.allowutf8);
        assertTrue(orig.cacheMultipart);
        // MimeBodyPart
        assertTrue(obp.setDefaultTextCharset);
        assertTrue(obp.setContentTypeFileName);
        assertFalse(obp.encodeFileName);
        assertFalse(obp.decodeFileName);
        assertTrue(obp.ignoreMultipartEncoding);
        assertTrue(obp.allowutf8);
        assertTrue(obp.cacheMultipart);
        // Change the properties in opposite way
        prop.put("mail.mime.setdefaulttextcharset", Boolean.FALSE);
        prop.put("mail.mime.setcontenttypefilename", Boolean.FALSE);
        prop.put("mail.mime.encodefilename", Boolean.TRUE);
        prop.put("mail.mime.decodefilename", Boolean.TRUE);
        prop.put("mail.mime.ignoremultipartencoding", Boolean.FALSE);
        prop.put("mail.mime.allowutf8", Boolean.FALSE);
        prop.put("mail.mime.cachemultipart", Boolean.FALSE);
        orig = buildFromProperties(prop);
        omp = (MimeMultipart) orig.getContent();
        obp = (MimeBodyPart) omp.getBodyPart(0);
        // MimeMessage
        assertFalse(orig.setDefaultTextCharset);
        assertFalse(orig.setContentTypeFileName);
        assertTrue(orig.encodeFileName);
        assertTrue(orig.decodeFileName);
        assertFalse(orig.ignoreMultipartEncoding);
        assertFalse(orig.allowutf8);
        assertFalse(orig.cacheMultipart);
        // MimeBodyPart
        assertFalse(obp.setDefaultTextCharset);
        assertFalse(obp.setContentTypeFileName);
        assertTrue(obp.encodeFileName);
        assertTrue(obp.decodeFileName);
        assertFalse(obp.ignoreMultipartEncoding);
        assertFalse(obp.allowutf8);
        assertFalse(obp.cacheMultipart);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    private MimeMessage buildFromProperties(Properties prop) throws MessagingException, IOException {
        Session s = Session.getInstance(prop);
        MimeMessage orig = createMessage(s);
        return orig;
    }

    private static MimeMessage createMessage(Session s) throws MessagingException {
        String content = "Mime-Version: 1.0\n" + "Subject: Example\n"
                + "Content-Type: multipart/mixed; boundary=\"-\"\n" + "\n" + "preamble\n" + "---\n"
                + "Content-Type: text/x-test\n" + "Content-Transfer-Encoding: quoted-printable\n" + "\n" + "test part\n"
                + "\n" + "-----\n";

        InputStream input = new AsciiStringInputStream(content);
        return new MimeMessage(s, input);
    }

    private static class AsciiStringInputStream extends InputStream {

        private final String input;
        private int position;

        public AsciiStringInputStream(String input) {
            this(input, true);
        }

        public AsciiStringInputStream(String input, boolean strict) {
            if (strict) {
                for (int i = 0; i < input.length(); i++) {
                    if (input.charAt(i) > 0x7F) {
                        throw new IllegalArgumentException("Not an ASCII string");
                    }
                }
            }
            this.input = input;
        }

        @Override
        public int read() {
            if (position < input.length()) {
                return input.charAt(position++) & 0xFF;
            } else {
                return -1;
            }
        }

    }
}
