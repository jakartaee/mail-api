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

import java.util.*;
import java.security.Provider;
import java.security.Security;
import javax.security.sasl.*;
import javax.security.auth.callback.*;

/**
 * Jakarta Mail SASL client factory for OAUTH2.
 *
 * @author Bill Shannon
 */
public class OAuth2SaslClientFactory implements SaslClientFactory {

    private static final String PROVIDER_NAME = "Jakarta-Mail-OAuth2";
    private static final String MECHANISM_NAME = "SaslClientFactory.XOAUTH2";

    static class OAuth2Provider extends Provider {
	private static final long serialVersionUID = -5371795551562287059L;

	public OAuth2Provider() {
	    super(PROVIDER_NAME, 1.0, "XOAUTH2 SASL Mechanism");
	    put(MECHANISM_NAME, OAuth2SaslClientFactory.class.getName());
	}
    }

    @Override
    public SaslClient createSaslClient(String[] mechanisms,
				String authorizationId, String protocol,
				String serverName, Map<String,?> props,
				CallbackHandler cbh) throws SaslException {
	for (String m : mechanisms) {
	    if (m.equals("XOAUTH2"))
		return new OAuth2SaslClient(props, cbh);
	}
	return null;
    }

    @Override
    public String[] getMechanismNames(Map<String,?> props) {
	return new String[] { "XOAUTH2" };
    }

    /**
     * Initialize this OAUTH2 provider, but only if there isn't one already.
     * If we're not allowed to add this provider, just give up silently.
     */
    public static void init() {
	try {
	    if (Security.getProvider(PROVIDER_NAME) == null)
		Security.addProvider(new OAuth2Provider());
	} catch (SecurityException ex) {
	    // oh well...
	}
    }
}
