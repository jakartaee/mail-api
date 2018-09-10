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

package com.sun.mail.imap.protocol;

import com.sun.mail.iap.ParsingException;
import com.sun.mail.iap.Response;
import com.sun.mail.imap.protocol.Status;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author tkrammer
 */
public class StratoImapBugfixTest {
    @Test
    public void testValidStatusResponseLeadingSpaces() throws Exception {
	final Response response =
		new Response("STATUS \"  Sent Items  \" (UIDNEXT 1)");
	final Status status = new Status(response);

	assertEquals("  Sent Items  ", status.mbox);
	assertEquals(1, status.uidnext);
    }

    @Test
    public void testValidStatusResponse() throws Exception {
	final Response response =
		new Response("STATUS \"Sent Items\" (UIDNEXT 1)");
	final Status status = new Status(response);

	assertEquals("Sent Items", status.mbox);
	assertEquals(1, status.uidnext);
    }

    @Test
    public void testInvalidStatusResponse() throws Exception {
	Response response = new Response("STATUS Sent Items (UIDNEXT 1)");
	final Status status = new Status(response);

	assertEquals("Sent Items", status.mbox);
	assertEquals(1, status.uidnext);
    }

    @Test
    public void testMissingBracket() throws Exception {
	final Response response =
		new Response("STATUS \"Sent Items\" UIDNEXT 1)");

	try {
	    new Status(response);
	    fail("Must throw exception");
	} catch(ParsingException e) {
	}
    }
}
