/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;

import org.junit.Test;

import com.sun.mail.imap.IMAPProvider;

import jakarta.mail.Provider;
import jakarta.mail.Session;

public class SessionTest {

    private static final String DEFAULT_PROVIDER = "com.sun.mail.util.DefaultProvider";

	@Test
	public void defaultProvider() {
		assertTrue(containsDefaultProvider(new IMAPProvider()));
		assertFalse(containsDefaultProvider(new Provider(Provider.Type.STORE, "imap", Object.class.getName(), "Oracle", null) {}));
	}
	
    private boolean containsDefaultProvider(Provider provider) {
        Annotation[] annotations = provider.getClass().getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            if (DEFAULT_PROVIDER.equals(annotation.annotationType().getName())) {
                return true;
            }
        }
        return false;
    }
}
