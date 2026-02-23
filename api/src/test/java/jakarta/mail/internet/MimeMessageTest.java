/*
 * Copyright (c) 2025, 2026 Oracle and/or its affiliates. All rights reserved.
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


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Properties;
import jakarta.mail.Session;
import org.junit.Test;

/**
 * Test the properties that control strict address parsing and UTF-8 support
 */
public class MimeMessageTest {

    @Test
    public void testDefaultProperties() throws Exception {
        Session session = Session.getInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        assertEquals(true, message.isStrict());
        assertEquals(false, message.isAllowutf8());
    }

    @Test
    public void testSystemAndSession() throws Exception {
        try {
            System.setProperty("mail.mime.address.strict", Boolean.TRUE.toString());
            System.setProperty("mail.mime.allowutf8", Boolean.FALSE.toString());
            Properties properties = new Properties();
            properties.setProperty("mail.mime.address.strict", Boolean.FALSE.toString());
            properties.setProperty("mail.mime.allowutf8", Boolean.TRUE.toString());
            Session session = Session.getInstance(properties);
            MimeMessage message = new MimeMessage(session);
            assertEquals(false, message.isStrict());
            assertEquals(true, message.isAllowutf8());
        } finally {
            System.clearProperty("mail.mime.address.strict");
            System.clearProperty("mail.mime.allowutf8");
        }
    }

    @Test
    public void testSystemPropertiesNoSession() throws Exception {
        try {
            System.setProperty("mail.mime.address.strict", Boolean.FALSE.toString());
            System.setProperty("mail.mime.allowutf8", Boolean.TRUE.toString());
            MimeMessage message = new MimeMessage((Session) null);
            assertEquals(false, message.isStrict());
            assertEquals(true, message.isAllowutf8());
        } finally {
            System.clearProperty("mail.mime.address.strict");
            System.clearProperty("mail.mime.allowutf8");
        }
    }

    @Test
    public void testSystemPropertiesAndSessionWithNoProperties() throws Exception {
        try {
            System.setProperty("mail.mime.address.strict", Boolean.FALSE.toString());
            System.setProperty("mail.mime.allowutf8", Boolean.TRUE.toString());
            Session session = Session.getInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            // System properties are ignored when Session is present (even if empty)
            assertEquals(true, message.isStrict());
            assertEquals(false, message.isAllowutf8());
        } finally {
            System.clearProperty("mail.mime.address.strict");
            System.clearProperty("mail.mime.allowutf8");
        }
    }

    @Test
    public void testEmptyHeaders() throws Exception {
        String base64Input = "ICAgIAogICAKCg==";
        byte[] input = Base64.getDecoder().decode(base64Input);
        Session session = Session.getDefaultInstance(new Properties());
        ByteArrayInputStream is = new ByteArrayInputStream(input);
        new MimeMessage(session, is);
    }
}
