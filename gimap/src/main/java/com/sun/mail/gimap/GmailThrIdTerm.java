/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.gimap;

import javax.mail.Message;
import javax.mail.search.StringTerm;

/**
 * This class implements searching for the Gmail thread ID.
 *
 * @since JavaMail 1.4.6
 * @author Bill Shannon
 */

public final class GmailThrIdTerm extends LongTerm {

    private static final long serialVersionUID = -4549268502183739744L;

    /**
     * Constructor.
     *
     * @param thrId  the thread ID
     */
    public GmailThrIdTerm(long thrId) {
	super(thrId);
    }

    /**
     * The match method.
     *
     * @param msg	the Message number is matched with this Message
     * @return		true if the match succeeds, otherwise false
     */
    public boolean match(Message msg) {
	long thrId;

	try {
	    if (msg instanceof GmailMessage)
		thrId = ((GmailMessage)msg).getThrId();
	    else
		return false;
	} catch (Exception e) {
	    return false;
	}

	return super.match(thrId);
    }

    /**
     * Equality comparison.
     *
     * @param	obj	the object to compare with
     * @return		true if equal
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof GmailThrIdTerm))
	    return false;
	return super.equals(obj);
    }
}
