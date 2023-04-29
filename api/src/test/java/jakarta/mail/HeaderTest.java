/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HeaderTest {

    @Test
    public void testEquals() {
        Header h1 = new Header("a", "b");
        Header h2 = new Header("a", "b");
        Header h3 = new Header("b", "a");
        Header h4 = new Header("a", "b") {
        };
        assertEquals(h1, h2);
        assertEquals(h1, h1);
        assertNotEquals(h1, h3);
        assertNotEquals(h1, null);
        assertNotEquals(h1, h4);
        assertNotEquals(h1, new Date());
    }
}
