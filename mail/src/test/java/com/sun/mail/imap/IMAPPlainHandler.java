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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.sun.mail.util.BASE64DecoderStream;

/**
 * Handle IMAP connection with PLAIN authentication.
 *
 * @author Bill Shannon
 */
public class IMAPPlainHandler extends IMAPHandler {

    protected String username = "test";
    protected String password = "test";

    public IMAPPlainHandler() {
	capabilities += " LOGINDISABLED AUTH=PLAIN";
    }

    /**
     * AUTHENTICATE PLAIN command.
     *
     * @throws IOException unable to read/write to socket
     */
    @Override
    public void authplain(String ir) throws IOException {
	if (ir == null) {
	    cont("");
	    ir = readLine();
	}
	String auth = new String(BASE64DecoderStream.decode(
				    ir.getBytes(StandardCharsets.US_ASCII)),
				StandardCharsets.UTF_8);
	String[] ap = auth.split("\000");
	String u = ap[1];
	String p = ap[2];
	//System.out.printf("USER: %s, PASSWORD: %s%n", u, p);
	if (!u.equals(username) || !p.equals(password)) {
	    no("authentication failed");
	    return;
	}
        ok("[CAPABILITY " + capabilities + "]");
    }
}
