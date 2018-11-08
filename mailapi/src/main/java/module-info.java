/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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
    exports javax.mail;
    exports javax.mail.event;
    exports javax.mail.internet;
    exports javax.mail.search;
    exports javax.mail.util;
    exports com.sun.mail.util;
    exports com.sun.mail.auth;
    exports com.sun.mail.handlers;

    requires transitive jakarta.activation;
    requires java.logging;
    requires java.xml;		// for text/xml handler
    requires java.desktop;	// for image/jpeg handler
    requires java.security.sasl; // for OAuth2 support
    uses javax.mail.Provider;
}
