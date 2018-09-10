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

import com.sun.mail.test.AsciiStringInputStream;
import com.sun.mail.test.NullOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.MessagingException;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the properties that control the MimeMultipart class.
 * Since the properties are now read in the parse method, all
 * these tests can be run in the same JVM.
 */
public class MimeMultipartPropertyTest {
 
    private static Session s = Session.getInstance(new Properties());

    /**
     * Clear all properties before each test.
     */
    @Before
    public void beforeTest() {
	clearAll();
    }

    @Test
    public void testBoundary() throws Exception {
	MimeMessage m = createMessage("x", "x", true);
	MimeMultipart mp = (MimeMultipart)m.getContent();
	assertEquals(mp.getCount(), 2);
    }

    @Test
    public void testBoundaryIgnore() throws Exception {
        System.setProperty(
	    "mail.mime.multipart.ignoreexistingboundaryparameter", "true");
	MimeMessage m = createMessage("x", "-", true);
	MimeMultipart mp = (MimeMultipart)m.getContent();
	assertEquals(mp.getCount(), 2);
    }

    @Test
    public void testBoundaryMissing() throws Exception {
	MimeMessage m = createMessage(null, "x", true);
	MimeMultipart mp = (MimeMultipart)m.getContent();
	assertEquals(mp.getCount(), 2);
    }

    @Test(expected=MessagingException.class)
    public void testBoundaryMissingEx() throws Exception {
        System.setProperty(
	    "mail.mime.multipart.ignoremissingboundaryparameter", "false");
	MimeMessage m = createMessage(null, "x", true);
	MimeMultipart mp = (MimeMultipart)m.getContent();
	mp.getCount();		// throw exception
	assertTrue(false);	// never get here
    }

    @Test
    public void testEndBoundaryMissing() throws Exception {
	MimeMessage m = createMessage("x", "x", false);
	MimeMultipart mp = (MimeMultipart)m.getContent();
	assertEquals(mp.getCount(), 2);
    }

    @Test(expected=MessagingException.class)
    public void testEndBoundaryMissingEx() throws Exception {
        System.setProperty(
	    "mail.mime.multipart.ignoremissingendboundary", "false");
	MimeMessage m = createMessage("x", "x", false);
	MimeMultipart mp = (MimeMultipart)m.getContent();
	mp.getCount();		// throw exception
	assertTrue(false);	// never get here
    }

    @Test
    public void testAllowEmpty() throws Exception {
        System.setProperty( "mail.mime.multipart.allowempty", "true");
	MimeMessage m = createEmptyMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	assertEquals(mp.getCount(), 0);
    }

    @Test(expected=MessagingException.class)
    public void testAllowEmptyEx() throws Exception {
	MimeMessage m = createEmptyMessage();
	MimeMultipart mp = (MimeMultipart)m.getContent();
	mp.getCount();		// throw exception
	assertTrue(false);	// never get here
    }

    @Test
    public void testAllowEmptyOutput() throws Exception {
        System.setProperty( "mail.mime.multipart.allowempty", "true");
	MimeMessage m = new MimeMessage(s);
	MimeMultipart mp = new MimeMultipart();
	m.setContent(mp);
	m.writeTo(new NullOutputStream());
	assertEquals(mp.getCount(), 0);
    }

    @Test(expected=IOException.class)
    public void testAllowEmptyOutputEx() throws Exception {
	MimeMessage m = new MimeMessage(s);
	MimeMultipart mp = new MimeMultipart();
	m.setContent(mp);
	m.writeTo(new NullOutputStream());	// throw exception
	assertTrue(false);	// never get here
    }

    /**
     * Clear all properties after all tests.
     */
    @AfterClass
    public static void after() {
        clearAll();
    }

    private static void clearAll() {
        System.clearProperty(
	    "mail.mime.multipart.ignoreexistingboundaryparameter");
        System.clearProperty(
	    "mail.mime.multipart.ignoremissingboundaryparameter");
        System.clearProperty(
	    "mail.mime.multipart.ignoremissingendboundary");
        System.clearProperty(
	    "mail.mime.multipart.allowempty");
    }

    /**
     * Create a test message.
     * If param is not null, it specifies the boundary parameter.
     * The actual boundary is specified by "actual".
     * If "end" is true, include the end boundary.
     */
    private static MimeMessage createMessage(String param, String actual,
				boolean end) throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Subject: Example\n" +
	    "Content-Type: multipart/mixed; " +
		(param != null ? "boundary=\"" + param + "\"" : "") + "\n" +
	    "\n" +
	    "preamble\n" +
	    "--" + actual + "\n" +
	    "\n" +
	    "first part\n" +
	    "\n" +
	    "--" + actual + "\n" +
	    "\n" +
	    "second part\n" +
	    "\n" +
	    (end ? "--" + actual + "--\n" : "");
 
	return new MimeMessage(s, new AsciiStringInputStream(content));
    }

    /**
     * Create a test message with no parts.
     */
    private static MimeMessage createEmptyMessage() throws MessagingException {
        String content =
	    "Mime-Version: 1.0\n" +
	    "Subject: Example\n" +
	    "Content-Type: multipart/mixed; boundary=\"x\"\n\n";
 
	return new MimeMessage(s, new AsciiStringInputStream(content));
    }
}
