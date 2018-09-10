/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test the Namespaces class.
 */
public class NamespacesTest {
    private static final String utf8Folder = "#public\u03b1/";
    private static final String utf7Folder = "#public&A7E-/";

    /**
     * Test an example NAMESPACE response.
     */
    @Test
    public void testAll() throws Exception {
	IMAPResponse response = new IMAPResponse(
	    "* NAMESPACE ((\"\" \"/\")) " +	// personal
	    "((\"~\" \"/\")) " +		// other users
	    "((\"#shared/\" \"/\")" +		// shared
		"(\"#public/\" \"/\")" +
		"(\"#ftp/\" \"/\")" +
		"(\"#news.\" \".\"))");
	Namespaces ns = new Namespaces(response);
	assertEquals(1, ns.personal.length);
	assertEquals("", ns.personal[0].prefix);
	assertEquals('/', ns.personal[0].delimiter);
	assertEquals(1, ns.otherUsers.length);
	assertEquals("~", ns.otherUsers[0].prefix);
	assertEquals('/', ns.otherUsers[0].delimiter);
	assertEquals(4, ns.shared.length);
	assertEquals("#shared/", ns.shared[0].prefix);
	assertEquals('/', ns.shared[0].delimiter);
	assertEquals("#public/", ns.shared[1].prefix);
	assertEquals('/', ns.shared[1].delimiter);
	assertEquals("#ftp/", ns.shared[2].prefix);
	assertEquals('/', ns.shared[2].delimiter);
	assertEquals("#news.", ns.shared[3].prefix);
	assertEquals('.', ns.shared[3].delimiter);
    }

    /**
     * Test an example NAMESPACE response with unnecessary spaces.
     */
    @Test
    public void testSpaces() throws Exception {
	IMAPResponse response = new IMAPResponse(
	    "* NAMESPACE ((\"\" \"/\")) " +	// personal
	    "( ( \"~\" \"/\" ) ) " +		// other users
	    "(( \"#shared/\" \"/\" )" +		// shared
		"( \"#public/\" \"/\" )" +
		"( \"#ftp/\" \"/\" )" +
		" (\"#news.\" \".\" ))");
	Namespaces ns = new Namespaces(response);
	assertEquals(1, ns.personal.length);
	assertEquals("", ns.personal[0].prefix);
	assertEquals('/', ns.personal[0].delimiter);
	assertEquals(1, ns.otherUsers.length);
	assertEquals("~", ns.otherUsers[0].prefix);
	assertEquals('/', ns.otherUsers[0].delimiter);
	assertEquals(4, ns.shared.length);
	assertEquals("#shared/", ns.shared[0].prefix);
	assertEquals('/', ns.shared[0].delimiter);
	assertEquals("#public/", ns.shared[1].prefix);
	assertEquals('/', ns.shared[1].delimiter);
	assertEquals("#ftp/", ns.shared[2].prefix);
	assertEquals('/', ns.shared[2].delimiter);
	assertEquals("#news.", ns.shared[3].prefix);
	assertEquals('.', ns.shared[3].delimiter);
    }

    /**
     * Test a NAMESPACE response with a UTF-7 folder name.
     */
    @Test
    public void testUtf7() throws Exception {
	IMAPResponse response = new IMAPResponse(
	    "* NAMESPACE ((\"\" \"/\")) " +	// personal
	    "((\"~\" \"/\")) " +		// other users
	    "((\"#shared/\" \"/\")" +		// shared
		"(\"" + utf7Folder + "\" \"/\")" +
		"(\"#ftp/\" \"/\")" +
		"(\"#news.\" \".\"))",
	    false);
	Namespaces ns = new Namespaces(response);
	assertEquals(1, ns.personal.length);
	assertEquals("", ns.personal[0].prefix);
	assertEquals('/', ns.personal[0].delimiter);
	assertEquals(1, ns.otherUsers.length);
	assertEquals("~", ns.otherUsers[0].prefix);
	assertEquals('/', ns.otherUsers[0].delimiter);
	assertEquals(4, ns.shared.length);
	assertEquals("#shared/", ns.shared[0].prefix);
	assertEquals('/', ns.shared[0].delimiter);
	assertEquals(utf8Folder, ns.shared[1].prefix);
	assertEquals('/', ns.shared[1].delimiter);
	assertEquals("#ftp/", ns.shared[2].prefix);
	assertEquals('/', ns.shared[2].delimiter);
	assertEquals("#news.", ns.shared[3].prefix);
	assertEquals('.', ns.shared[3].delimiter);
    }

    /**
     * Test a NAMESPACE response with a UTF-8 folder name.
     */
    @Test
    public void testUtf8() throws Exception {
	IMAPResponse response = new IMAPResponse(
	    "* NAMESPACE ((\"\" \"/\")) " +	// personal
	    "((\"~\" \"/\")) " +		// other users
	    "((\"#shared/\" \"/\")" +		// shared
		"(\"" + utf8Folder + "\" \"/\")" +
		"(\"#ftp/\" \"/\")" +
		"(\"#news.\" \".\"))",
	    true);
	Namespaces ns = new Namespaces(response);
	assertEquals(1, ns.personal.length);
	assertEquals("", ns.personal[0].prefix);
	assertEquals('/', ns.personal[0].delimiter);
	assertEquals(1, ns.otherUsers.length);
	assertEquals("~", ns.otherUsers[0].prefix);
	assertEquals('/', ns.otherUsers[0].delimiter);
	assertEquals(4, ns.shared.length);
	assertEquals("#shared/", ns.shared[0].prefix);
	assertEquals('/', ns.shared[0].delimiter);
	assertEquals(utf8Folder, ns.shared[1].prefix);
	assertEquals('/', ns.shared[1].delimiter);
	assertEquals("#ftp/", ns.shared[2].prefix);
	assertEquals('/', ns.shared[2].delimiter);
	assertEquals("#news.", ns.shared[3].prefix);
	assertEquals('.', ns.shared[3].delimiter);
    }
}
