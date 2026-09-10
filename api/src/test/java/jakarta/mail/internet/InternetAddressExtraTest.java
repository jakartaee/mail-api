/*
 * Copyright (c) 1997, 2023 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Test Internet address parsing that can't be tested using paramterized test.
 *
 * @author Bill Shannon
 */

public class InternetAddressExtraTest {
    @Test
    public void testNewlineInDomainLiteral() throws Exception {
        InternetAddress ia = new InternetAddress("test@[\r\nfoo]", "test");
        try {
            ia.validate();
            fail("validation succeeded");
        } catch (AddressException ex) {
            // success!
        }
    }

    @Test
    public void testNewlineInLocal() throws Exception {
        InternetAddress ia =
                new InternetAddress("\"test\r\nfoo\"@example.com", "test");
        try {
            ia.validate();
            fail("validation succeeded");
        } catch (AddressException ex) {
            // success!
        }
    }

    @Test
    public void testNewlineInLocalWithWhitespace() throws Exception {
        InternetAddress ia =
                new InternetAddress("\"test\r\n foo\"@example.com", "test");
        ia.validate();
        // success!
    }

    @Test
    public void testIdnRejectionChainsCause() throws Exception {
        // U+FDD0 is a Unicode noncharacter — not letter-or-digit, so the
        // fast-path domain check defers to java.net.IDN, which rejects it
        // as a prohibited code point. The IAE from IDN should travel along
        // as the chained cause.
        System.setProperty("mail.mime.allowutf8", "true");
        try {
            InternetAddress ia =
                    new InternetAddress("arnt@foo﷐.example", true);
            fail("expected rejection, got " + ia);
        } catch (AddressException ex) {
            Throwable cause = ex.getCause();
            assertNotNull("AddressException should carry IDN's IAE as cause",
                    cause);
            assertSame(IllegalArgumentException.class, cause.getClass());
        } finally {
            System.clearProperty("mail.mime.allowutf8");
        }
    }
}
