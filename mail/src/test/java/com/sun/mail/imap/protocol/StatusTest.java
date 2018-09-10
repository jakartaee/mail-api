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
 * Test the Status class.
 */
public class StatusTest {
    /**
     * Test that the returned mailbox name is decoded.
     */
    @Test
    public void testMailboxDecode() throws Exception {
	String mbox = "Entw\u00fcrfe";
	IMAPResponse response = new IMAPResponse(
	    "* STATUS " +
	    BASE64MailboxEncoder.encode(mbox) +
	    " (MESSAGES 231 UIDNEXT 44292)", false);
	Status s = new Status(response);
	assertEquals(mbox, s.mbox);
	assertEquals(231, s.total);
	assertEquals(44292, s.uidnext);
    }

    /**
     * Test that the returned mailbox name is correct when using UTF-8.
     */
    @Test
    public void testMailboxUtf8() throws Exception {
	String mbox = "Entw\u00fcrfe";
	IMAPResponse response = new IMAPResponse(
	    "* STATUS " +
	    mbox +
	    " (MESSAGES 231 UIDNEXT 44292)", true);
	Status s = new Status(response);
	assertEquals(mbox, s.mbox);
	assertEquals(231, s.total);
	assertEquals(44292, s.uidnext);
    }

    /**
     * Test that spaces in the response don't confuse it.
     */
    @Test
    public void testSpaces() throws Exception {
	IMAPResponse response = new IMAPResponse(
	    "* STATUS  test  ( MESSAGES  231  UIDNEXT  44292 )");
	Status s = new Status(response);
	assertEquals("test", s.mbox);
	assertEquals(231, s.total);
	assertEquals(44292, s.uidnext);
    }

    /**
     * Test that a bad response throws a ParsingException
     */
    @Test(expected = ParsingException.class)
    public void testBadResponseNoAttrList() throws Exception {
	String mbox = "test";
	IMAPResponse response = new IMAPResponse("* STATUS test ");
	Status s = new Status(response);
    }

    /**
     * Test that a bad response throws a ParsingException
     */
    @Test(expected = ParsingException.class)
    public void testBadResponseNoAttrs() throws Exception {
	String mbox = "test";
	IMAPResponse response = new IMAPResponse("* STATUS test (");
	Status s = new Status(response);
    }
}
