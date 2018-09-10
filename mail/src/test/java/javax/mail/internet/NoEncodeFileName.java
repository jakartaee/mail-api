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

package javax.mail.internet;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 * Test "mail.mime.encodefilename" System property not set.
 */
public class NoEncodeFileName {
 
    protected static String fileName;

    // a bunch of non-ASCII characters
    private static String encodedFileName =
	"=?utf-8?B?w4DDgcOFw4bDgMOBw4XDhsOHw4jDicOKw4vDjMONw47Dj8OQw4DD" +
	"gcOFw4bDh8OIw4nDisOLw4zDjcOOw4/DkMORw5LDk8OUw5XDlsOYw5nDmsObw5" +
	"zDncOew5/DoMOhw6LDo8Okw6XDpsOnw6jDqcOqw6vDrMOtw67Dr8Oww7HDssOz" +
	"w7TDtcO2w7jDucO6w7vDvMO9w77Dv8OAw4HDhcOGw4cuZG9j?=";

    static {
	try {
	    fileName = MimeUtility.decodeText(encodedFileName);
	} catch (UnsupportedEncodingException ex) {
	    // should never happen
	}
	
    }

    // RFC 2231 encoding
    private static String expected =
	"utf-8''%C3%80%C3%81%C3%85%C3%86%C3%80%C3%81%C3%85%C3%86%C3%87%C3%88" +
	"%C3%89%C3%8A%C3%8B%C3%8C%C3%8D%C3%8E%C3%8F%C3%90%C3%80%C3%81%C3%85" +
	"%C3%86%C3%87%C3%88%C3%89%C3%8A%C3%8B%C3%8C%C3%8D%C3%8E%C3%8F%C3%90" +
	"%C3%91%C3%92%C3%93%C3%94%C3%95%C3%96%C3%98%C3%99%C3%9A%C3%9B%C3%9C" +
	"%C3%9D%C3%9E%C3%9F%C3%A0%C3%A1%C3%A2%C3%A3%C3%A4%C3%A5%C3%A6%C3%A7" +
	"%C3%A8%C3%A9%C3%AA%C3%AB%C3%AC%C3%AD%C3%AE%C3%AF%C3%B0%C3%B1%C3%B2" +
	"%C3%B3%C3%B4%C3%B5%C3%B6%C3%B8%C3%B9%C3%BA%C3%BB%C3%BC%C3%BD%C3%BE" +
	"%C3%BF%C3%80%C3%81%C3%85%C3%86%C3%87.doc";

    @BeforeClass
    public static void before() {
	System.out.println("NoEncodeFileName");
	System.setProperty("mail.mime.charset", "utf-8");
	System.clearProperty("mail.mime.encodefilename");
    }

    @Test
    public void test() throws Exception {
	MimeBodyPart mbp = new MimeBodyPart();
	mbp.setText("test");
	mbp.setFileName(fileName);
	mbp.updateHeaders();
	String h = mbp.getHeader("Content-Type", "");
	assertTrue(h.contains("name*="));
	assertTrue(h.contains(expected));
	h = mbp.getHeader("Content-Disposition", "");
	assertTrue(h.contains("filename*="));
	assertTrue(h.contains(expected));
    }

    @AfterClass
    public static void after() {
	// should be unnecessary
	System.clearProperty("mail.mime.charset");
	System.clearProperty("mail.mime.encodefilename");
	System.clearProperty("mail.mime.encodeparameters");
    }
}
