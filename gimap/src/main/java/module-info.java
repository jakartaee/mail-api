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

module com.sun.mail.gimap {
    exports com.sun.mail.gimap;
    exports com.sun.mail.gimap.protocol;
    provides javax.mail.Provider with
	com.sun.mail.gimap.GmailProvider, com.sun.mail.gimap.GmailSSLProvider;

    requires jakarta.mail;
    /*
    following is intentionally optional/static for gimap module to work
    with com.sun.mail:jakarta.mail (all-in-one bundle jar)
    as well as with com.sun.mail:mailapi + com.sun.mail:imap
    the latter deps come through maven dependency tree, therefore
    they must be explicitely excluded if the former is being used
    to avoid having two jars having same 'jakarta.mail' module name
    in the environement
    */
    requires static com.sun.mail.imap;
}
