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

import javax.mail.internet.ParameterList;

import com.sun.mail.iap.Response;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test the BODYSTRUCTURE class.
 */
public class BODYSTRUCTURETest {
    /**
     * Test workaround for Exchange bug that returns NIL instead of ""
     * for a parameter with an empty value (name="").
     */
    @Test
    public void testExchangeEmptyParameterValueBug() throws Exception {
	IMAPResponse response = new IMAPResponse(
    "* 3 FETCH (BODYSTRUCTURE ((\"text\" \"plain\" (\"charset\" \"UTF-8\") " +
    "NIL NIL \"quoted-printable\" 512 13 NIL (\"inline\" NIL) NIL NIL)" +
    "(\"text\" \"html\" (\"charset\" \"UTF-8\") NIL NIL \"quoted-printable\" " +
    "784 11 NIL (\"inline\" NIL) NIL NIL) \"alternative\" " +
    "(\"boundary\" \"__139957996218379.example.com\" \"name\" NIL) NIL NIL))");
    // here's the incorrect NIL that should be "" ............^
	FetchResponse fr = new FetchResponse(response);
	BODYSTRUCTURE bs = fr.getItem(BODYSTRUCTURE.class);
	ParameterList p = bs.cParams;
	assertNotNull(p.get("name"));
    }

    /**
     * Test workaround for Exchange bug that returns the Content-Description
     * header value instead of the Content-Disposition for some kinds of
     * (formerly S/MIME encrypted?) messages.
     */
    @Test
    public void testExchangeBadDisposition() throws Exception {
	IMAPResponse response = new IMAPResponse(
    "* 1 FETCH (BODYSTRUCTURE (" +
	"(\"text\" \"plain\" (\"charset\" \"us-ascii\") NIL NIL \"7bit\" " +
	    "21 0 NIL (\"inline\" NIL) NIL NIL)" +
	"(\"application\" \"octet-stream\" (\"name\" \"private.txt\") " +
	    "NIL NIL \"base64\" 690 NIL " +
		"(\"attachment\" (\"filename\" \"private.txt\")) NIL NIL) " +
    "\"mixed\" (\"boundary\" \"----=_Part_0_-1731707885.1504253815584\") " +
	"\"S/MIME Encrypted Message\" NIL))");
    //    ^^^^^^^ here's the string that should be the disposition
	FetchResponse fr = new FetchResponse(response);
	BODYSTRUCTURE bs = fr.getItem(BODYSTRUCTURE.class);
	assertEquals("S/MIME Encrypted Message", bs.description);
    }
}
