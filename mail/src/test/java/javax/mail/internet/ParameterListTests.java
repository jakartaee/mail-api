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
import static org.junit.Assert.assertTrue;

/**
 * XXX - add more tests
 */
public class ParameterListTests {

    @BeforeClass
    public static void before() {
	System.out.println("ParameterListTests");
	System.clearProperty("mail.mime.windowsfilenames");
	System.clearProperty("mail.mime.applefilenames");
    }

    /**
     * Test that backslashes are properly removed.
     */
    @Test
    public void testBackslash() throws Exception {
	System.clearProperty("mail.mime.windowsfilenames");
	ParameterList pl = new ParameterList("; filename=\"\\a\\b\\c.txt\"");
	assertEquals(pl.get("filename"), "abc.txt");
    }

    /**
     * Test that a long parameter that's been split into segments
     * is parsed correctly.
     */
    @Test
    public void testLongParse() throws Exception {
	String p0 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	String p1 = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
	ParameterList pl = new ParameterList("; p*0="+p0+"; p*1="+p1);
	assertEquals(p0 + p1, pl.get("p"));
    }

    /**
     * Test that a long parameter that's set programmatically is split
     * into segments.
     */
    @Test
    public void testLongSet() throws Exception {
	String p0 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	String p1 = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
	ParameterList pl = new ParameterList();
	pl.set("p", p0 + p1);
	assertEquals(p0 + p1, pl.get("p"));
	String pls = pl.toString();
	assertTrue(pls.indexOf("p*0=") >= 0);
	assertTrue(pls.indexOf("p*1=") >= 0);
    }
}
