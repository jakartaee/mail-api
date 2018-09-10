/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test the property that contols ContentDisposition non-strict mode
 */
public class ContentDispositionNoStrict {

    @BeforeClass
    public static void before() {
        System.setProperty("mail.mime.contentdisposition.strict", "false");
    }

    @Test
    public void testDecode() throws Exception {
        try {
            ContentDisposition cd = new ContentDisposition("\"/non/standard/stuff/here.csv\"");
            assertNull("Content disposition must parse to null in non-strict mode", cd.getDisposition());
        } catch (ParseException px) {
            fail("Exception must not be thrown in non-strict mode");
        }
    }

    @Test
    public void testDecodeWithParams() throws Exception {
        try {
            ContentDisposition cd = new ContentDisposition(" ; size=12345");
            assertNull("Content disposition must parse to null in non-strict mode", cd.getDisposition());
        } catch (ParseException px) {
            fail("Exception must not be thrown in non-strict mode");
        }
    }

    @AfterClass
    public static void after() {
        System.clearProperty("mail.mime.contentdisposition.strict");
    }
}

