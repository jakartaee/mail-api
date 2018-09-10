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

package com.sun.mail.smtp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.Level;

import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.ASCIIUtility;

/**
 * Handle connection with LOGIN or PLAIN authentication.
 *
 * @author Bill Shannon
 */
public class SMTPLoginHandler extends SMTPHandler {
    protected String username = "test";
    protected String password = "test";

    /**
     * EHLO command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    @Override
    public void ehlo() throws IOException {
        println("250-hello");
        println("250-SMTPUTF8");
        println("250-8BITMIME");
        println("250 AUTH PLAIN LOGIN");
    }

    /**
     * AUTH command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    @Override
    public void auth(String line) throws IOException {
        StringTokenizer ct = new StringTokenizer(line, " ");
        String commandName = ct.nextToken().toUpperCase();
	String mech = ct.nextToken().toUpperCase();
	String ir = "";
	if (ct.hasMoreTokens())
	    ir = ct.nextToken();

	if (LOGGER.isLoggable(Level.FINE))
	    LOGGER.fine(line);
	if (mech.equalsIgnoreCase("PLAIN"))
	    plain(ir);
	else if (mech.equalsIgnoreCase("LOGIN"))
	    login(ir);
	else
	    println("501 bad AUTH mechanism");
    }

    /**
     * AUTH LOGIN
     */
    private void login(String ir) throws IOException {
	println("334");
	// read user name
	String resp = readLine();
	if (!isBase64(resp)) {
	    println("501 response not base64");
	    return;
	}
	byte[] response = resp.getBytes(StandardCharsets.US_ASCII);
	response = BASE64DecoderStream.decode(response);
	String u = new String(response, StandardCharsets.UTF_8);
	if (LOGGER.isLoggable(Level.FINE))
	    LOGGER.fine("USER: " + u);
	println("334");

	// read password
	resp = readLine();
	if (!isBase64(resp)) {
	    println("501 response not base64");
	    return;
	}
	response = resp.getBytes(StandardCharsets.US_ASCII);
	response = BASE64DecoderStream.decode(response);
	String p = new String(response, StandardCharsets.UTF_8);
	if (LOGGER.isLoggable(Level.FINE))
	    LOGGER.fine("PASSWORD: " + p);

	//System.out.printf("USER: %s, PASSWORD: %s%n", u, p);
	if (!u.equals(username) || !p.equals(password)) {
	    println("535 authentication failed");
	    return;
	}

	println("235 Authenticated");
    }

    /**
     * AUTH PLAIN
     */
    private void plain(String ir) throws IOException {
	String auth = new String(BASE64DecoderStream.decode(
				    ir.getBytes(StandardCharsets.US_ASCII)),
				StandardCharsets.UTF_8);
	String[] ap = auth.split("\000");
	String u = ap[1];
	String p = ap[2];
	//System.out.printf("USER: %s, PASSWORD: %s%n", u, p);
	if (!u.equals(username) || !p.equals(password)) {
	    println("535 authentication failed");
	    return;
	}
	println("235 Authenticated");
    }

    /**
     * Is every character in the string a base64 character?
     */
    private boolean isBase64(String s) {
	int len = s.length();
	if (s.endsWith("=="))
	    len -= 2;
	else if (s.endsWith("="))
	    len--;
	for (int i = 0; i < len; i++) {
	    char c = s.charAt(i);
	    if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
		    (c >= '0' && c <= '9') || c == '+' || c == '/'))
		return false;
	}
	return true;
    }
}
