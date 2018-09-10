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

package com.sun.mail.iap;

import java.io.*;
import java.util.Properties;

import com.sun.mail.test.NullOutputStream;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test the Protocol class.
 */
public final class ProtocolTest {

    private static final byte[] noBytes = new byte[0];
    private static final PrintStream nullps =
				    new PrintStream(new NullOutputStream());
    private static final ByteArrayInputStream nullis =
				    new ByteArrayInputStream(noBytes);

    /**
     * Test that the tag prefix is computed properly.
     */
    @Test
    public void testTagPrefix() throws IOException, ProtocolException {
	Protocol.tagNum.set(0);		// reset for testing
	String tag = newProtocolTag();
	assertEquals("A0", tag);
	for (int i = 1; i < 26; i++)
	    tag = newProtocolTag();
	assertEquals("Z0", tag);
	tag = newProtocolTag();
	assertEquals("AA0", tag);
	for (int i = 26 + 1; i < (26*26 + 26); i++)
	    tag = newProtocolTag();
	assertEquals("ZZ0", tag);
	tag = newProtocolTag();
	assertEquals("AAA0", tag);
	for (int i = 26*26 + 26 + 1; i < (26*26*26 + 26*26 + 26); i++)
	    tag = newProtocolTag();
	assertEquals("ZZZ0", tag);
	tag = newProtocolTag();
	// did it wrap around?
	assertEquals("A0", tag);
    }

    private String newProtocolTag() throws IOException, ProtocolException {
	Properties props = new Properties();
	Protocol p = new Protocol(nullis, nullps, props, false);
	String tag = p.writeCommand("CMD", null);
	return tag;
    }

    /**
     * Test that the tag prefix is reused.
     */
    @Test
    public void testTagPrefixReuse() throws IOException, ProtocolException {
	Properties props = new Properties();
	props.setProperty("mail.imap.reusetagprefix", "true");
	Protocol p = new Protocol(nullis, nullps, props, false);
	String tag = p.writeCommand("CMD", null);
	assertEquals("A0", tag);
	p = new Protocol(nullis, nullps, props, false);
	tag = p.writeCommand("CMD", null);
	assertEquals("A0", tag);
    }
}
