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
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.security.sasl.*;
import javax.security.auth.callback.*;

import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.ASCIIUtility;

/**
 * Handle connection with SASL authentication.
 *
 * @author Bill Shannon
 */
public class SMTPSaslHandler extends SMTPHandler {

    /**
     * EHLO command.
     *
     * @throws IOException
     *             unable to read/write to socket
     */
    @Override
    public void ehlo() throws IOException {
        println("250-hello");
        println("250 AUTH DIGEST-MD5");
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

	final String u = "test";
	final String p = "test";
	final String realm = "test";

	CallbackHandler cbh = new CallbackHandler() {
	    @Override
	    public void handle(Callback[] callbacks) {
		if (LOGGER.isLoggable(Level.FINE))
		    LOGGER.fine("SASL callback length: " + callbacks.length);
		for (int i = 0; i < callbacks.length; i++) {
		    if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("SASL callback " + i + ": " + callbacks[i]);
		    if (callbacks[i] instanceof NameCallback) {
			NameCallback ncb = (NameCallback)callbacks[i];
			ncb.setName(u);
		    } else if (callbacks[i] instanceof PasswordCallback) {
			PasswordCallback pcb = (PasswordCallback)callbacks[i];
			pcb.setPassword(p.toCharArray());
		    } else if (callbacks[i] instanceof AuthorizeCallback) {
			AuthorizeCallback ac = (AuthorizeCallback)callbacks[i];
			if (LOGGER.isLoggable(Level.FINE))
			    LOGGER.fine("SASL authorize: " +
				"authn: " + ac.getAuthenticationID() + ", " +
				"authz: " + ac.getAuthorizationID() + ", " +
				"authorized: " + ac.getAuthorizedID());
			ac.setAuthorized(true);
		    } else if (callbacks[i] instanceof RealmCallback) {
			RealmCallback rcb = (RealmCallback)callbacks[i];
			rcb.setText(realm != null ?
				    realm : rcb.getDefaultText());
		    } else if (callbacks[i] instanceof RealmChoiceCallback) {
			RealmChoiceCallback rcb =
			    (RealmChoiceCallback)callbacks[i];
			if (realm == null)
			    rcb.setSelectedIndex(rcb.getDefaultChoice());
			else {
			    // need to find specified realm in list
			    String[] choices = rcb.getChoices();
			    for (int k = 0; k < choices.length; k++) {
				if (choices[k].equals(realm)) {
				    rcb.setSelectedIndex(k);
				    break;
				}
			    }
			}
		    }
		}
	    }
	};

	SaslServer ss;
	try {
	    ss = Sasl.createSaslServer(mech, "smtp", "localhost", null, cbh);
	} catch (SaslException sex) {
	    LOGGER.log(Level.FINE, "Failed to create SASL server", sex);
	    println("501 Failed to create SASL server");
	    return;
	}
	if (ss == null) {
	    LOGGER.fine("No SASL support");
	    println("501 No SASL support");
	    return;
	}
	if (LOGGER.isLoggable(Level.FINE))
	    LOGGER.fine("SASL server " + ss.getMechanismName());

	byte[] response = ir.getBytes();
	while (!ss.isComplete()) {
	    try {
		byte[] chal = ss.evaluateResponse(response);
		// send challenge
		if (LOGGER.isLoggable(Level.FINE))
		    LOGGER.fine("SASL challenge: " +
			ASCIIUtility.toString(chal, 0, chal.length));
		byte[] ba = BASE64EncoderStream.encode(chal);
		if (ba.length > 0)
		    println("334 " +
			    ASCIIUtility.toString(ba, 0, ba.length));
		else
		    println("334");
		// read response
		String resp = readLine();
		if (!isBase64(resp)) {
		    println("501 response not base64");
		break;
		}
		response = resp.getBytes();
		response = BASE64DecoderStream.decode(response);
	    } catch (SaslException ex) {
		println("501 " + ex.toString());
		break;
	    }
	}

	if (ss.isComplete() /*&& status == SUCCESS*/) {
	    String qop = (String)ss.getNegotiatedProperty(Sasl.QOP);
	    if (qop != null && (qop.equalsIgnoreCase("auth-int") ||
				qop.equalsIgnoreCase("auth-conf"))) {
		// XXX - NOT SUPPORTED!!!
		LOGGER.fine(
			"SASL Mechanism requires integrity or confidentiality");
		println("501 " +
			"SASL Mechanism requires integrity or confidentiality");
		return;
	    }
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
