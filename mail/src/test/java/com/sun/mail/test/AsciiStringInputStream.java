/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.test;

import java.io.InputStream;

/**
 * Replacement for deprecated java.io.StringBufferInputStream
 */
public class AsciiStringInputStream extends InputStream {

    private final String input;
    private int position;

    public AsciiStringInputStream(String input) {
	this(input, true);
    }

    public AsciiStringInputStream(String input, boolean strict) {
	if (strict) {
	    for (int i = 0; i < input.length(); i++) {
		if (input.charAt(i) > 0x7F) {
		    throw new IllegalArgumentException("Not an ASCII string");
		}
	    }
	}

	this.input = input;
    }

    @Override
    public int read() {
	if (position < input.length()) {
	    return input.charAt(position++) & 0xFF;
	} else {
	    return -1;
	}
    }

}
