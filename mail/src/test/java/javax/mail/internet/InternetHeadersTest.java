/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.mail.test.AsciiStringInputStream;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;

import javax.mail.*;

import org.junit.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test the InternetHeaders class.
 */
public class InternetHeadersTest {
 
    private static final String initialWhitespaceHeader =
						" \r\nSubject: test\r\n\r\n";
    private static final String initialContinuationHeader =
						" Subject: test\r\n\r\n";

    /**
     * Test that a continuation line is handled properly.
     */
    @Test
    public void testContinuationLine() throws Exception {
	String header = "Subject: a\r\n b\r\n\r\n";
	InternetHeaders ih = new InternetHeaders(
		new AsciiStringInputStream(header));
	assertEquals(1, ih.getHeader("Subject").length);
	assertEquals("a\r\n b", ih.getHeader("Subject")[0]);
    }

    /**
     * Test that a whitespace line at the beginning is ignored.
     */
    @Test
    public void testInitialWhitespaceLineConstructor() throws Exception {
	InternetHeaders ih = new InternetHeaders(
		new AsciiStringInputStream(initialWhitespaceHeader));
	testInitialWhitespaceLine(ih);
    }

    /**
     * Test that a whitespace line at the beginning is ignored.
     */
    @Test
    public void testInitialWhitespaceLineLoad() throws Exception {
	InternetHeaders ih = new InternetHeaders();
	ih.load(new AsciiStringInputStream(initialWhitespaceHeader));
	testInitialWhitespaceLine(ih);
    }

    private void testInitialWhitespaceLine(InternetHeaders ih)
				throws Exception {
	assertEquals(1, ih.getHeader("Subject").length);
	assertEquals("test", ih.getHeader("Subject")[0]);
	Enumeration<Header> e = ih.getAllHeaders();
	while (e.hasMoreElements()) {
	    Header h = e.nextElement();
	    assertEquals("Subject", h.getName());
	    assertEquals("test", h.getValue());
	}
    }

    /**
     * Test that a continuation line at the beginning is handled.
     */
    @Test
    public void testInitialContinuationLineConstructor() throws Exception {
	InternetHeaders ih = new InternetHeaders(
		new AsciiStringInputStream(initialContinuationHeader));
	testInitialContinuationLine(ih);
    }

    /**
     * Test that a continuation line at the beginning is handled.
     */
    @Test
    public void testInitialContinuationLineLoad() throws Exception {
	InternetHeaders ih = new InternetHeaders();
	ih.load(new AsciiStringInputStream(initialContinuationHeader));
	testInitialContinuationLine(ih);
    }

    private void testInitialContinuationLine(InternetHeaders ih)
				throws Exception {
	assertEquals(1, ih.getHeader("Subject").length);
	assertEquals("test", ih.getHeader("Subject")[0]);
	Enumeration<Header> e = ih.getAllHeaders();
	while (e.hasMoreElements()) {
	    Header h = e.nextElement();
	    assertEquals("Subject", h.getName());
	    assertEquals("test", h.getValue());
	}
    }
}
