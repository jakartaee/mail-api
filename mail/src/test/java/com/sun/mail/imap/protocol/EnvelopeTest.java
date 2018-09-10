/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.mail.iap.Response;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test the ENVELOPE class.
 */
public class EnvelopeTest {
    /**
     * Test workaround for Yahoo IMAP bug that returns a bogus space
     * character when one of the recipients is "undisclosed-recipients".
     */
    @Test
    public void testYahooUndisclosedRecipientsBug() throws Exception {
	IMAPResponse response = new IMAPResponse(
    "* 2 FETCH (INTERNALDATE \"24-Apr-2012 20:28:58 +0000\" " +
    "RFC822.SIZE 155937 " +
    "ENVELOPE (\"Wed, 28 Sep 2011 11:16:17 +0100\" \"test\" " +
    "((NIL NIL \"xxx\" \"tju.edu.cn\")) " +
    "((NIL NIL \"xxx\" \"gmail.com\")) " +
    "((NIL NIL \"xxx\" \"tju.edu.cn\")) " +
    "((\"undisclosed-recipients\" NIL " +
	"\"\\\"undisclosed-recipients\\\"\" NIL )) " +
    // here's the space inserted by Yahoo IMAP ^
    "NIL NIL NIL " +
    "\"<xxx@mail.gmail.com>\"))");
	FetchResponse fr = new FetchResponse(response);
	// no exception means it worked
    }

    /**
     * Test workaround for Yahoo IMAP bug that returns an empty list
     * instad of NIL for some addresses in ENVELOPE response.
     */
    @Test
    public void testYahooEnvelopeAddressListBug() throws Exception {
	IMAPResponse response = new IMAPResponse(
    "* 2 FETCH (RFC822.SIZE 2567 INTERNALDATE \"29-Apr-2011 13:49:01 +0000\" " +
    "ENVELOPE (\"Fri, 29 Apr 2011 19:19:01 +0530\" \"test\" " +
    "((\"xxx\" NIL \"xxx\" \"milium.com.br\")) " +
    "((\"xxx\" NIL \"xxx\" \"milium.com.br\")) " +
    "((NIL NIL \"xxx\" \"live.hk\")) () NIL NIL NIL " +
    "\"<20110429134718.70333732030A@mail2.milium.com.br>\"))");
	FetchResponse fr = new FetchResponse(response);
	// no exception means it worked
    }
}
