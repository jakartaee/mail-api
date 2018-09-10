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
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.security.sasl.*;
import javax.security.auth.callback.*;

import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.ASCIIUtility;

/**
 * Handle IMAP connection with SASL authentication.
 *
 * @author Bill Shannon
 */
public class IMAPSaslHandler extends IMAPHandler {

    public IMAPSaslHandler() {
	capabilities += " LOGINDISABLED AUTH=DIGEST-MD5";
    }

    /**
     * AUTHENTICATE command.
     *
     * @throws IOException unable to read/write to socket
     */
    @Override
    public void authenticate(String mech, String ir) throws IOException {
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
	    ss = Sasl.createSaslServer(mech, "imap", "localhost", null, cbh);
	} catch (SaslException sex) {
	    LOGGER.log(Level.FINE, "Failed to create SASL server", sex);
	    no("Failed to create SASL server");
	    return;
	}
	if (ss == null) {
	    LOGGER.fine("No SASL support");
	    no("No SASL support");
	    return;
	}
	if (LOGGER.isLoggable(Level.FINE))
	    LOGGER.fine("SASL server " + ss.getMechanismName());

	byte[] response = new byte[0];
	while (!ss.isComplete()) {
	    try {
		byte[] chal = ss.evaluateResponse(response);
		if (ss.isComplete()) {
		    break;
		} else {
		    // send challenge
		    if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("SASL challenge: " +
			    ASCIIUtility.toString(chal, 0, chal.length));
		    byte[] ba = BASE64EncoderStream.encode(chal);
		    if (ba.length > 0)
			cont(ASCIIUtility.toString(ba, 0, ba.length));
		    else
			cont();
		    // read response
		    String resp = readLine();
		    response = resp.getBytes();
		    response = BASE64DecoderStream.decode(response);
		}
	    } catch (SaslException ex) {
		no(ex.toString());
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
		no("SASL Mechanism requires integrity or confidentiality");
		return;
	    }
	}

        ok("[CAPABILITY " + capabilities + "]");
    }
}
