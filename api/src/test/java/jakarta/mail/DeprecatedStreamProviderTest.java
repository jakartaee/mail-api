/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import jakarta.mail.BodyPart.DeprecatedStreamProviderWrapper;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

import org.junit.Test;

public class DeprecatedStreamProviderTest {

    @Test
    public void testDeprecatedStreamProviderInBodyPart() {
        BodyPart bodyPart = new MimeBodyPart();
        assertEquals(DeprecatedStreamProviderWrapper.class, bodyPart.streamProvider.getClass());
        // Cannot test the warning, we have to check the log manually
        bodyPart.streamProvider.inputBase64(new ByteArrayInputStream(new byte[0]));
//        Sep 19, 2025 1:52:27 PM jakarta.mail.BodyPart$DeprecatedStreamProviderWrapper warn
//        WARNING: [DEPRECATED] Accessing deprecated field 'streamProvider'. Use jakarta.mail.internet.MimeBodyPart#getStreamProvider() instead.

    }

    @Test
    public void testDeprecatedStreamProviderInMultiPart() {
        Multipart multiPart = new MimeMultipart();
        assertEquals(DeprecatedStreamProviderWrapper.class, multiPart.streamProvider.getClass());
        // Cannot test the warning, we have to check the log manually
        multiPart.streamProvider.inputBase64(new ByteArrayInputStream(new byte[0]));
//        Sep 19, 2025 1:52:27 PM jakarta.mail.BodyPart$DeprecatedStreamProviderWrapper warn
//        WARNING: [DEPRECATED] Accessing deprecated field 'streamProvider'. Use jakarta.mail.internet.MimeMultipart#getStreamProvider() instead.
    }
}
