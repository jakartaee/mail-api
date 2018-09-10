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

import javax.mail.search.SearchTerm;

/**
 * This class implements a long integer search term.
 *
 * @since JavaMail 1.4.6
 * @author Bill Shannon
 */

abstract class LongTerm extends SearchTerm {
    /**
     * The number.
     *
     * @serial
     */
    protected long number;

    private static final long serialVersionUID = 5285147193246128043L;

    protected LongTerm(long number) {
	this.number = number;
    }

    /**
     * Return the number to compare with.
     *
     * @return	the number
     */
    public long getNumber() {
	return number;
    }

    protected boolean match(long i) {
	return i == number;
    }

    /**
     * Equality comparison.
     */
    public boolean equals(Object obj) {
	if (!(obj instanceof LongTerm))
	    return false;
	LongTerm t = (LongTerm)obj;
	return t.number == this.number && super.equals(obj);
    }

    /**
     * Compute a hashCode for this object.
     */
    public int hashCode() {
	return (int)number + super.hashCode();
    }
}
