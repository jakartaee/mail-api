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
 * This class implements searching for the Gmail message ID.
 *
 * @since JavaMail 1.4.6
 * @author Bill Shannon
 */

public final class GmailMsgIdTerm extends LongTerm {

    private static final long serialVersionUID = -7090909401071626866L;

    /**
     * Constructor.
     *
     * @param msgId  the message ID
     */
    public GmailMsgIdTerm(long msgId) {
	super(msgId);
    }

    /**
     * The match method.
     *
     * @param msg	the Message number is matched with this Message
     * @return		true if the match succeeds, otherwise false
     */
    public boolean match(Message msg) {
	long msgId;

	try {
	    if (msg instanceof GmailMessage)
		msgId = ((GmailMessage)msg).getMsgId();
	    else
		return false;
	} catch (Exception e) {
	    return false;
	}

	return super.match(msgId);
    }

    /**
     * Equality comparison.
     *
     * @param	obj	the object to compare with
     * @return		true if equal
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof GmailMsgIdTerm))
	    return false;
	return super.equals(obj);
    }
}
