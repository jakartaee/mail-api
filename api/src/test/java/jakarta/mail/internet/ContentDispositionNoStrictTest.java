/*
 * Copyright (c) 2009, 2023 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the property that controls ContentDisposition non-strict mode
 */
public class ContentDispositionNoStrictTest {

    @BeforeAll
    public static void before() {
        System.setProperty("mail.mime.contentdisposition.strict", "false");
    }

    @Test
    public void testDecode() throws Exception {
        try {
            ContentDisposition cd = new ContentDisposition("\"/non/standard/stuff/here.csv\"");
            assertNull(cd.getDisposition(), "Content disposition must parse to null in non-strict mode");
        } catch (ParseException px) {
            fail("Exception must not be thrown in non-strict mode");
        }
    }

    @Test
    public void testDecodeWithParams() throws Exception {
        try {
            ContentDisposition cd = new ContentDisposition(" ; size=12345");
            assertNull(cd.getDisposition(), "Content disposition must parse to null in non-strict mode");
        } catch (ParseException px) {
            fail("Exception must not be thrown in non-strict mode");
        }
    }

    @AfterAll
    public static void after() {
        System.clearProperty("mail.mime.contentdisposition.strict");
    }
}

