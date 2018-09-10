/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 * Test the MimeMultipart.addFrom method, for bug 5057742.
 */
public class AddFromTest {
 
    private static final Session s = Session.getInstance(new Properties());
    private static final String ADDR = "a@example.com";
    private static final InternetAddress iaddr;
    private static final InternetAddress[] addresses;
    static {
	InternetAddress ia = null;
	try {
	    ia = new InternetAddress(ADDR);
	} catch (AddressException ex) {
	    // can't happen
	} finally {
	    iaddr = ia;
	}
	addresses = new InternetAddress[] { iaddr };
    }

    @Test
    public void testNoFrom() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.addFrom(addresses);
	assertEquals("Number of From headers", 1, m.getHeader("From").length);
	assertEquals("From header", ADDR, m.getHeader("From", ","));
    }

    @Test
    public void testOneFrom() throws Exception {
        MimeMessage m = new MimeMessage(s);
	m.setFrom(iaddr);
	m.addFrom(addresses);
	assertEquals("Number of From headers", 1, m.getHeader("From").length);
	assertEquals("From header", ADDR + ", " + ADDR,
	    m.getHeader("From", ","));
    }
}
