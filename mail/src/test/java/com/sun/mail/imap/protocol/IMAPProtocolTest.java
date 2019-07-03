/*
 * Copyright (c) 2014, 2019 Oracle and/or its affiliates. All rights reserved.
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

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;
import com.sun.mail.test.AsciiStringInputStream;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test the IMAPProtocol class.
 */
public class IMAPProtocolTest {
    private static final boolean debug = false;
    private static final String content = "aXQncyBteSB0ZXN0IG1haWwNCg0K\r\n";
    private static final String response =
	    "* 1 FETCH (UID 127 BODY[1.1.MIME] {82}\r\n" +
	    "Content-Type: text/plain;\r\n" +
	    "\tcharset=\"utf-8\"\r\n" +
	    "Content-Transfer-Encoding: base64\r\n" +
	    "\r\n" +
	    " ENVELOPE (\"Mon, 17 Mar 2014 14:03:08 +0100\" \"test invoice\"" +
	    " ((\"Joe User\" NIL \"joe.user\" \"example.com\"))" +
	    " ((\"Joe User\" NIL \"joe.user\" \"example.com\"))" +
	    " ((\"Joe User\" NIL \"joe.user\" \"example.com\"))" +
	    " ((\"Joe User\" NIL \"joe.user\" \"example.com\"))" + 
	    " NIL NIL NIL \"<1234@example.com>\") BODY[1.1]<0> " +
	    "{" + content.length() + "}\r\n" + content + 
	    ")\r\n" +
	    "A0 OK FETCH completed.\r\n";

    /**
     * Test that a response containing multiple BODY elements
     * returns the correct one.  Derived from a customer bug
     * with Exchange 2003.  Normally this would never happen,
     * but it's a valid IMAP response and Jakarta Mail needs to
     * handle it properly.
     */
    @Test
    public void testMultipleBodyResponses() throws Exception {
	Properties props = new Properties();
	props.setProperty("mail.imap.reusetagprefix", "true");
	IMAPProtocol p = new IMAPProtocol(
	    new AsciiStringInputStream(response),
	    new PrintStream(new ByteArrayOutputStream()),
	    props,
	    debug);
	BODY b = p.fetchBody(1, "1.1");
	assertEquals("section number", "1.1", b.getSection());
	//System.out.println(b);
	//System.out.write(b.getByteArray().getNewBytes());
	String result = new String(b.getByteArray().getNewBytes(), "us-ascii");
	assertEquals("getByteArray.getNewBytes", content, result);
	InputStream is = b.getByteArrayInputStream();
	byte[] ba = new byte[is.available()];
	is.read(ba);
	result = new String(ba, "us-ascii");
	assertEquals("getByteArrayInputStream", content, result);
    }

    /**
     * Same test as above, but using a different fetchBody method.
     */
    @Test
    public void testMultipleBodyResponses2() throws Exception {
	Properties props = new Properties();
	props.setProperty("mail.imap.reusetagprefix", "true");
	IMAPProtocol p = new IMAPProtocol(
	    new AsciiStringInputStream(response),
	    new PrintStream(new ByteArrayOutputStream()),
	    props,
	    debug);
	BODY b = p.fetchBody(1, "1.1", 0, content.length(), null);
	assertEquals("section number", "1.1", b.getSection());
	//System.out.println(b);
	//System.out.write(b.getByteArray().getNewBytes());
	String result = new String(b.getByteArray().getNewBytes(), "us-ascii");
	assertEquals("getByteArray.getNewBytes", content, result);
	InputStream is = b.getByteArrayInputStream();
	byte[] ba = new byte[is.available()];
	is.read(ba);
	result = new String(ba, "us-ascii");
	assertEquals("getByteArrayInputStream", content, result);
    }
}
