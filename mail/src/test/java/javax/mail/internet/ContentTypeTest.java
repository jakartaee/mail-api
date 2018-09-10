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

package javax.mail.internet;

import org.junit.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test the ContentType class.
 *
 * XXX - for now, just some simple regression tests for reported bugs.
 */
public class ContentTypeTest {
 
    @Test
    public void testMatch() throws Exception {
	ContentType empty = new ContentType();
	assertFalse(empty.match("text/plain"));
	ContentType plain = new ContentType("text/plain");
	assertTrue(plain.match("text/plain"));
	assertFalse(empty.match(plain));
	assertFalse(plain.match(empty));
	assertTrue(plain.match("text/*"));
	ContentType text = new ContentType("text/*");
	assertTrue(text.match(plain));
	assertTrue(plain.match(text));
    }
}
