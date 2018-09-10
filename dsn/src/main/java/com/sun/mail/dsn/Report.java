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

package com.sun.mail.dsn;

/**
 * An abstract report type, to be included in a MultipartReport.
 * Subclasses define specific report types, such as {@link DeliveryStatus}
 * and {@link DispositionNotification}.
 *
 * @since	JavaMail 1.4.2
 */
public abstract class Report {
    protected String type;	// the MIME subtype of the report

    /**
     * Construct a report of the indicated MIME subtype.
     * The primary MIME type is always "message".
     *
     * @param	type	the MIME subtype
     */
    protected Report(String type) {
	this.type = type;
    }

    /**
     * Get the MIME subtype of the report.
     * The primary MIME type is always "message".
     *
     * @return	the MIME subtype
     */
    public String getType() {
	return type;
    }
}
