/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 * Test "mail.mime.encodefilename" System property not set and
 * "mail.mime.encodeparameters" set to "false".
 */
public class NoEncodeFileNameNoEncodeParameters extends NoEncodeFileName {
 
    @BeforeClass
    public static void before() {
	System.out.println("NoEncodeFileNameNoEncodeParameters");
	System.setProperty("mail.mime.charset", "utf-8");
	System.setProperty("mail.mime.encodeparameters", "false");
	// assume mail.mime.encodefilename defaults to false
	System.clearProperty("mail.mime.encodefilename");
    }

    @Test
    public void test() throws Exception {
	MimeBodyPart mbp = new MimeBodyPart();
	mbp.setText("test");
	mbp.setFileName(fileName);
	mbp.updateHeaders();
	String h = mbp.getHeader("Content-Type", "");
	assertTrue(h.contains("name="));
	assertTrue(h.contains(fileName));
	h = mbp.getHeader("Content-Disposition", "");
	assertTrue(h.contains("filename="));
	assertTrue(h.contains(fileName));
    }
}
