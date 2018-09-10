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
 * Handle IMAP connection with LOGIN authentication.
 *
 * @author Bill Shannon
 */
public class IMAPLoginHandler extends IMAPHandler {

    protected String username = "test";
    protected String password = "test";

    public IMAPLoginHandler() {
	capabilities += " LOGINDISABLED AUTH=LOGIN";
    }

    /**
     * AUTHENTICATE LOGIN command.
     *
     * @throws IOException unable to read/write to socket
     */
    public void authlogin(String ir) throws IOException {
	if (ir != null)
	    bad("AUTHENTICATE LOGIN does not support initial response");
	cont(base64encode("Username"));
	String resp = readLine();
	String u = new String(BASE64DecoderStream.decode(
				    resp.getBytes(StandardCharsets.US_ASCII)),
				StandardCharsets.UTF_8);
	cont(base64encode("Password"));
	resp = readLine();
	String p = new String(BASE64DecoderStream.decode(
				    resp.getBytes(StandardCharsets.US_ASCII)),
				StandardCharsets.UTF_8);
	//System.out.printf("USER: %s, PASSWORD: %s%n", u, p);
	if (!u.equals(username) || !p.equals(password)) {
	    no("authentication failed");
	    return;
	}
        ok("[CAPABILITY " + capabilities + "]");
    }
}
