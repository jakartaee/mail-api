/*
 * Copyright (c) 2010, 2019 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test that the "mail.mime.allowutf8" System property
 * not set doesn't allow UTF-8 data to be read.
 */
public class LineInputStreamUtf8Fail {

    @BeforeClass
    public static void before() {
	System.out.println("LineInputStreamUtf8Fail");
	System.clearProperty("mail.mime.allowutf8");
    }

    @Test
    public void testUtf8() throws Exception {
	LineInputStream is = new LineInputStream(new ByteArrayInputStream(
			"a\u00A9b\n".getBytes(StandardCharsets.UTF_8)), false);
	assertEquals("a\302\251b", is.readLine());
    }

    @Test
    public void testIso() throws IOException {
	LineInputStream is = new LineInputStream(new ByteArrayInputStream(
		    "a\251b\n".getBytes(StandardCharsets.ISO_8859_1)), false);
	assertEquals("a\251b", is.readLine());
    }
}
