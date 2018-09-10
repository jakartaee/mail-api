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

package javax.mail.internet;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test that non-ASCII file names are encoded by default.
 */
public class NonAsciiFileNames {

    private static String charset;

    @BeforeClass
    public static void before() {
	System.out.println("NonAsciiFileNames");
	charset = System.getProperty("mail.mime.charset");
	System.setProperty("mail.mime.charset", "utf-8");
    }

    /**
     * Test that non-ASCII filenames are encoded by default.
     */
    @Test
    public void testNonAsciiFileName() throws Exception {
	MimeBodyPart mbp = new MimeBodyPart();
	mbp.setText("test\n");
	mbp.setFileName("test\u00a1\u00a2\u00a3");
	MimeBodyPart.updateHeaders(mbp);

	String s = mbp.getHeader("Content-Disposition", null);
	assertTrue("Content-Disposition filename", s.indexOf("filename*") >= 0);
	s = mbp.getHeader("Content-Type", null);
	assertTrue("Content-Type name", s.indexOf("name*") >= 0);
    }

    /**
     * Test that non-ASCII filenames are encoded by default.
     * Make sure an existing Content-Type header is updated.
     */
    @Test
    public void testNonAsciiFileNameWithContentType() throws Exception {
	MimeBodyPart mbp = new MimeBodyPart();
	mbp.setText("test\n");
	mbp.setHeader("Content-Type", "text/x-test");
	mbp.setFileName("test\u00a1\u00a2\u00a3");
	MimeBodyPart.updateHeaders(mbp);

	String s = mbp.getHeader("Content-Disposition", null);
	assertTrue("Content-Disposition filename", s.indexOf("filename*") >= 0);
	s = mbp.getHeader("Content-Type", null);
	assertTrue("Content-Type name", s.indexOf("name*") >= 0);
    }

    @AfterClass
    public static void after() {
	if (charset == null)
	    System.clearProperty("mail.mime.charset");
	else
	    System.setProperty("mail.mime.charset", charset);
    }
}
