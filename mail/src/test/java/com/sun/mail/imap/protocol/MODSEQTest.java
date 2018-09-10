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
 * Test the MODSEQ class.
 */
public class MODSEQTest {
    /**
     * Test an example MODSEQ response.
     */
    @Test
    public void testAll() throws Exception {
	IMAPResponse response = new IMAPResponse(
	    "* 1 FETCH (MODSEQ (624140003))");
	FetchResponse fr = new FetchResponse(response);
	MODSEQ m = fr.getItem(MODSEQ.class);
	assertEquals(1, m.seqnum);
	assertEquals(624140003, m.modseq);
    }

    /**
     * Test an example MODSEQ response with unnecessary spaces.
     */
    @Test
    public void testSpaces() throws Exception {
	IMAPResponse response = new IMAPResponse(
	    "* 1 FETCH ( MODSEQ ( 624140003 ) )");
	FetchResponse fr = new FetchResponse(response);
	MODSEQ m = fr.getItem(MODSEQ.class);
	assertEquals(1, m.seqnum);
	assertEquals(624140003, m.modseq);
    }
}
