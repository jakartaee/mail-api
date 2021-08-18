/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.mail.MessagingException;

/**
 * The exception thrown due to an error in parsing RFC822 
 * or MIME headers, including multipart bodies.
 *
 * @author John Mani
 */

public class ParseException extends MessagingException {

    private static final long serialVersionUID = 7649991205183658089L;

    /**
     * Constructs a ParseException with no detail message.
     */
    public ParseException() {
	super();
    }

    /**
     * Constructs a ParseException with the specified detail message.
     * @param s		the detail message
     */
    public ParseException(String s) {
	super(s);
    }
}
