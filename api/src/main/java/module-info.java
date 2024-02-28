/*
 * Copyright (c) 2018, 2023 Oracle and/or its affiliates. All rights reserved.
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

/**
 * Jakarta Mail API
 */
module jakarta.mail {

    requires java.logging;
    requires transitive jakarta.activation;

    exports jakarta.mail;
    exports jakarta.mail.event;
    exports jakarta.mail.internet;
    exports jakarta.mail.search;
    exports jakarta.mail.util;

    uses jakarta.mail.Provider;
    uses jakarta.mail.util.StreamProvider;
    //reflective call to java.beans.Beans.instantiate
    requires static java.desktop;
}
