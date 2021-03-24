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

package com.sun.mail.stream;

import jakarta.mail.stream.StreamProvider;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class BinaryStream implements StreamProvider {

    private final String[] encodings = new String[] {BINARY_ENCODER, BIT7_ENCODER, BIT8_ENCODER};

    @Override
    public String[] keys() {
        return encodings;
    }

    @Override
    public InputStream from(InputStream in, Map<String, Object> parameters) {
        return in;
    }

    @Override
    public OutputStream from(OutputStream out, Map<String, Object> parameters) {
        return out;
    }
}
