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
 * This class implements searching using the Gmail X-GM-RAW extension.
 *
 * @since JavaMail 1.4.6
 * @author Bill Shannon
 */

public final class GmailRawSearchTerm extends StringTerm {

    private static final long serialVersionUID = 6284730140424242662L;

    /**
     * Constructor.
     *
     * @param pattern  the pattern to search for
     */
    public GmailRawSearchTerm(String pattern) {
	// Note: comparison is case-insensitive
	super(pattern);
    }

    /**
     * The match method.
     *
     * @param msg	the pattern match is applied to this Message's 
     *			subject header
     * @exception	RuntimeException	this can't be supported locally
     */
    public boolean match(Message msg) {
	throw new RuntimeException("GmailRawSearchTerm not supported locally");
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof GmailRawSearchTerm))
	    return false;
	return super.equals(obj);
    }
}
