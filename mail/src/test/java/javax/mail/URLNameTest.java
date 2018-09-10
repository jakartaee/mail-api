/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package javax.mail;

import java.net.URL;

import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 * Test the URLName class.
 *
 * XXX - for now, just some simple regression tests for reported bugs.
 */
public class URLNameTest {
 
    @Test
    public void testReflexiveEquality() throws Exception {
	URLName u = new URLName("test");
	assertEquals(u, u);	// bug 6365
	u = new URLName("imap://test.com/INBOX");
	assertEquals(u, u);
    }

    /**
     * Test that the getFile method returns the file part *without*
     * the separator character.  This behavior is different than the
     * URL or URI classes but needs to be preserved for compatibility.
     */
    @Test
    public void testFile() throws Exception {
	URLName u = new URLName("http://host/file");
	assertEquals("file", u.getFile());
	u = new URLName("http://host:123/file");
	assertEquals("file", u.getFile());
	u = new URLName("http://host/");
	assertEquals("", u.getFile());
	u = new URLName("http://host");
	assertEquals(null, u.getFile());
	u = new URLName("http://host:123");
	assertEquals(null, u.getFile());
    }

    /**
     * Test that the getURL method returns a URL with the same value
     * as the URLName.
     */
    @Test
    public void testURL() throws Exception {
	// Note: must use a protocol supported by the URL class
	URLName u = new URLName("http://host/file");
	assertEquals("file", u.getFile());
	URL url = u.getURL();
	assertEquals(u.toString(), url.toString());
	u = new URLName("http://host:123/file");
	url = u.getURL();
	assertEquals(u.toString(), url.toString());
	u = new URLName("http://host:123/");
	url = u.getURL();
	assertEquals(u.toString(), url.toString());
	u = new URLName("http://host:123");
	url = u.getURL();
	assertEquals(u.toString(), url.toString());
    }
}
