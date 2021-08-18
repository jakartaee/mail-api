/*
 * Copyright (c) 2018, 2021 Oracle and/or its affiliates. All rights reserved.
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

module jakarta.mail {
    exports jakarta.mail;
    exports jakarta.mail.event;
    exports jakarta.mail.internet;
    exports jakarta.mail.search;
    exports jakarta.mail.util;

    requires transitive jakarta.activation;
    requires transitive java.logging;
    requires java.xml;		// for text/xml handler
    requires java.desktop;	// for image/jpeg handler
    requires transitive java.security.sasl; // for OAuth2 support
    uses jakarta.mail.Provider;
    uses jakarta.mail.util.StreamProvider;
}
