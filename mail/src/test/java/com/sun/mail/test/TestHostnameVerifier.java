/*
 * Copyright (c) 1997, 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * {@link HostnameVerifier} implementation intended to be used for unit tests.
 */
public class TestHostnameVerifier implements HostnameVerifier {
    /*
     * This is based on an assumption that the hostname verifier is instantiated
     * by its default constructor in a managed way.
     *
     * Unit tests that check this property should impose their own thread safety.
     * For example, when executing code expected to be using the TestHostnameVerifier,
     * the unit test may synchronize on the TestHostnameVerifier class and call the
     * static "reset" method prior to de-synchronizing.
     */
    public static int defaultConstructorCount = 0;
    private boolean acceptConnections = true;
    private boolean used = false;

    public TestHostnameVerifier() {
        defaultConstructorCount++;
    }

    public TestHostnameVerifier(boolean acceptConnections) {
        this.acceptConnections = acceptConnections;
    }

    @Override
    public boolean verify(String hostname, SSLSession session) {
        used = true;
        return acceptConnections;
    }

    /**
     * Indicates whether the hostname verifier has been used.
     * @return
     */
    public boolean hasBeenUsed() {
        return used;
    }

    /**
     * Used to reset static values.
     */
    public static void reset() {
        defaultConstructorCount = 0;
    }
}
