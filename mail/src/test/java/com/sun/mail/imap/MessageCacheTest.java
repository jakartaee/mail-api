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

package com.sun.mail.imap;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test the IMAP MessageCache.
 */
public class MessageCacheTest {
    /**
     * Test that when a message is expunged and a new message is added,
     * the new message has the expected sequence number.
     */
    @Test
    public void testExpungeAdd() throws Exception {
	// test a range of values to find boundary condition errors
	for (int n = 1; n <= 100; n++) {
	    //System.out.println("MessageCache.testExpungeAdd: test " + n);
	    // start with one message
	    MessageCache mc = new MessageCache(1, false);
	    // add the remaining messages (eat into SLOP)
	    mc.addMessages(n - 1, 2);
	    // now expunge a message to cause the seqnums array to be created
	    mc.expungeMessage(1);
	    // and add one more message
	    mc.addMessages(1, n);
	    //System.out.println("  new seqnum " + mc.seqnumOf(n + 1));
	    // does the new message have the expected sequence number?
	    assertEquals(mc.seqnumOf(n + 1), n);
	}
    }

    /**
     * Test that when a message is expunged and new messages are added,
     * the new messages have the expected sequence number.  Similar to
     * the above, but the seqnums array is created first, then expanded.
     */
    @Test
    public void testExpungeAddExpand() throws Exception {
	// test a range of values to find boundary condition errors
	for (int n = 2; n <= 100; n++) {
	    //System.out.println("MessageCache.testExpungeAdd: test " + n);
	    // start with two messages
	    MessageCache mc = new MessageCache(2, false);
	    // now expunge a message to cause the seqnums array to be created
	    mc.expungeMessage(1);
	    // add the remaining messages (eat into SLOP)
	    mc.addMessages(n - 1, 2);
	    //System.out.println("  new seqnum " + mc.seqnumOf(n + 1));
	    // does the new message have the expected sequence number?
	    assertEquals(mc.seqnumOf(n + 1), n);
	}
    }
}
