/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package javax.mail.internet;

import org.junit.*;

/**
 * Test "mail.mime.encodefilename" System property set to "true"
 * and "mail.mime.encodeparameters" set to "false".
 */
public class EncodeFileNameNoEncodeParameters extends EncodeFileName {
 
    @BeforeClass
    public static void before() {
	System.out.println("EncodeFileNameNoEncodeParameters");
	System.setProperty("mail.mime.charset", "utf-8");
	System.setProperty("mail.mime.encodefilename", "true");
	System.setProperty("mail.mime.encodeparameters", "false");
    }
}
