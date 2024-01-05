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

package jakarta.mail.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DummyStreamProvider implements StreamProvider {
    
    public DummyStreamProvider() {
    }

    @Override
    public InputStream inputBase64(InputStream in) {
        return null;
    }

    @Override
    public OutputStream outputBase64(OutputStream out) {
        return null;
    }

    @Override
    public InputStream inputBinary(InputStream in) {
        return null;
    }

    @Override
    public OutputStream outputBinary(OutputStream out) {
        return null;
    }

    @Override
    public OutputStream outputB(OutputStream out) {
        return null;
    }

    @Override
    public InputStream inputQ(InputStream in) {
        return null;
    }

    @Override
    public OutputStream outputQ(OutputStream out, boolean encodingWord) {
        return null;
    }

    @Override
    public LineInputStream inputLineStream(InputStream in, boolean allowutf8) {
        return new LineInputStream() {
            @Override
            public String readLine() throws IOException {
                return null;
            }
        };
    }

    @Override
    public LineOutputStream outputLineStream(OutputStream out, boolean allowutf8) {
        return null;
    }

    @Override
    public InputStream inputQP(InputStream in) {
        return null;
    }

    @Override
    public OutputStream outputQP(OutputStream out) {
        return null;
    }

    @Override
    public InputStream inputSharedByteArray(byte[] buff) {
        return null;
    }

    @Override
    public InputStream inputUU(InputStream in) {
        return null;
    }

    @Override
    public OutputStream outputUU(OutputStream out, String filename) {
        return null;
    }

}