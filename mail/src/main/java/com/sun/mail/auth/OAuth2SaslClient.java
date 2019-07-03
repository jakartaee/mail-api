/*
 * Copyright (c) 2014, 2019 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.security.Provider;
import java.security.Security;
import javax.security.sasl.*;
import javax.security.auth.callback.*;

import com.sun.mail.util.ASCIIUtility;

/**
 * Jakarta Mail SASL client for OAUTH2.
 *
 * @see <a href="http://tools.ietf.org/html/rfc6749">
 *	RFC 6749 - OAuth 2.0 Authorization Framework</a>
 * @see <a href="http://tools.ietf.org/html/rfc6750">
 *	RFC 6750 - OAuth 2.0 Authorization Framework: Bearer Token Usage</a>
 * @author Bill Shannon
 */
public class OAuth2SaslClient implements SaslClient {
    private CallbackHandler cbh;
    //private Map<String,?> props;	// XXX - not currently used
    private boolean complete = false;

    public OAuth2SaslClient(Map<String,?> props, CallbackHandler cbh) {
	//this.props = props;
	this.cbh = cbh;
    }

    @Override
    public String getMechanismName() {
	return "XOAUTH2";
    }

    @Override
    public boolean hasInitialResponse() {
	return true;
    }

    @Override
    public byte[] evaluateChallenge(byte[] challenge) throws SaslException {
	if (complete)
	    return new byte[0];

	NameCallback ncb = new NameCallback("User name:");
	PasswordCallback pcb = new PasswordCallback("OAuth token:", false);
	try {
	    cbh.handle(new Callback[] { ncb, pcb });
	} catch (UnsupportedCallbackException ex) {
	    throw new SaslException("Unsupported callback", ex);
	} catch (IOException ex) {
	    throw new SaslException("Callback handler failed", ex);
	}

	/*
	 * The OAuth token isn't really a password, and Jakarta Mail doesn't
	 * use char[] for passwords, so we don't worry about storing the
	 * token in strings.
	 */
	String user = ncb.getName();
	String token = new String(pcb.getPassword());
	pcb.clearPassword();
	String resp = "user=" + user + "\001auth=Bearer " + token + "\001\001";
	byte[] response;
	try {
	    response = resp.getBytes("utf-8");
	} catch (UnsupportedEncodingException ex) {
	    // fall back to ASCII
	    response = ASCIIUtility.getBytes(resp);
	}
	complete = true;
	return response;
    }

    @Override
    public boolean isComplete() {
	return complete;
    }

    @Override
    public byte[] unwrap(byte[] incoming, int offset, int len)
				throws SaslException {
	throw new IllegalStateException("OAUTH2 unwrap not supported");
    }

    @Override
    public byte[] wrap(byte[] outgoing, int offset, int len)
				throws SaslException {
	throw new IllegalStateException("OAUTH2 wrap not supported");
    }

    @Override
    public Object getNegotiatedProperty(String propName) {
	if (!complete)
	    throw new IllegalStateException("OAUTH2 getNegotiatedProperty");
	return null;
    }

    @Override
    public void dispose() throws SaslException {
    }
}
