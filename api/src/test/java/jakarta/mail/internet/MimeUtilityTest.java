/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MimeUtilityTest {

    @Test
    public void issue812() {
        // Same value in both files
        assertEquals("ISO-8859-1", MimeUtility.mimeCharset("8859_1"));
        // Conflict value, javamail.charset.map wins
        assertEquals("ISO-8859-1", MimeUtility.mimeCharset("iso8859_1"));
        // Exists only in javamail.charset.map
        assertEquals("ISO-8859-3", MimeUtility.mimeCharset("8859_3"));
        // Exists only in jakarta.charset.map
        assertEquals("ISO-8859-4", MimeUtility.mimeCharset("8859_4"));
    }

}
