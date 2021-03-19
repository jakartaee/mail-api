/*
 * Copyright (c) 2015, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail.internet;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;

import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 * Test "mail.mime.encodefilename" System property set.
 */
public class EncodeFileNameTest extends NoEncodeFileNameTest {
 
    // depends on exactly how MimeUtility.encodeText splits long words
    private static String expected1 =
	"=?utf-8?B?w4DDgcOFw4bDgMOBw4XDhsOHw4jDicOKw4vDjMONw47Dj8OQw4DDgcOF?=";
    private static String expected2 =
	"=?utf-8?B?w4bDh8OIw4nDisOLw4zDjcOOw4/DkMORw5LDk8OUw5XDlsOYw5nDmsObw5w=?=";
    private static String expected3 =
	"=?utf-8?B?w53DnsOfw6DDocOiw6PDpMOlw6bDp8Oow6nDqsOrw6zDrcOuw6/DsMOx?=";
    private static String expected4 =
	"=?utf-8?B?w7LDs8O0w7XDtsO4w7nDusO7w7zDvcO+w7/DgMOBw4XDhsOHLmRvYw==?=";

    @BeforeClass
    public static void before() {
	System.out.println("EncodeFileName");
	System.setProperty("mail.mime.charset", "utf-8");
	System.setProperty("mail.mime.encodefilename", "true");
	// assume mail.mime.encodeparameters defaults to true
	System.clearProperty("mail.mime.encodeparameters");
    }

    @Test
    @Override
    public void test() throws Exception {
	MimeBodyPart mbp = new MimeBodyPart();
	mbp.setText("test");
	mbp.setFileName(fileName);
	mbp.updateHeaders();
	String h = mbp.getHeader("Content-Type", "");
	assertTrue(h.contains("name="));
	assertTrue(h.contains(expected1));
	assertTrue(h.contains(expected2));
	assertTrue(h.contains(expected3));
	assertTrue(h.contains(expected4));
	h = mbp.getHeader("Content-Disposition", "");
	assertTrue(h.contains("filename="));
	assertTrue(h.contains(expected1));
	assertTrue(h.contains(expected2));
	assertTrue(h.contains(expected3));
	assertTrue(h.contains(expected4));
    }
}
