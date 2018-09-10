/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.util;

import java.io.ByteArrayOutputStream;
import com.sun.mail.util.QPEncoderStream;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test quoted-printable encoder.
 *
 * @author Bill Shannon
 */

public class QPEncoderStreamTest {
    /**
     * Test that a trailing space is encoded in the output stream.
     */
    @Test
    public void testTrailingSpace() throws Exception {
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	QPEncoderStream qs = new QPEncoderStream(bos);
	qs.write("test ".getBytes("us-ascii"));
	qs.flush();
	String result = new String(bos.toByteArray(), "us-ascii");
	assertEquals("test=20", result);
    }
}
