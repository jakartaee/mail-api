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
import static org.junit.Assert.assertEquals;

/**
 * Test that the "mail.mime.windowsfilenames" System property
 * causes the filename to be returned with backslashes preserved.
 */
public class WindowsFileNames {

    @BeforeClass
    public static void before() {
	System.out.println("WindowsFileNames");
	System.setProperty("mail.mime.windowsfilenames", "true");
    }

    @Test
    public void testProp() throws Exception {
	ParameterList pl = new ParameterList("; filename=\"\\a\\b\\c.txt\"");
	assertEquals(pl.get("filename"), "\\a\\b\\c.txt");
    }

    @AfterClass
    public static void after() {
	// should be unnecessary
	System.clearProperty("mail.mime.windowsfilenames");
    }
}
