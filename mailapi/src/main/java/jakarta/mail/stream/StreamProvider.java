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

package jakarta.mail.stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface StreamProvider {

    public static final String BASE_64_ENCODER = "base64";
    public static final String B_ENCODER = "b";
    public static final String Q_ENCODER = "q";
    public static final String BINARY_ENCODER = "binary";
    public static final String BIT7_ENCODER = "7bit";
    public static final String BIT8_ENCODER = "8bit";
    public static final String QUOTED_PRINTABLE_ENCODER = "quoted-printable";
    public static final String UU_ENCODER = "uuencode";
    public static final String X_UU_ENCODER = "x-uuencode";
    public static final String X_UUE = "x-uue";
    public static final String LINE_STREAM = "line";
    public static final String SHARED_STREAM = "shared";
    
    String[] keys();

    InputStream from(InputStream in, Map<String, Object> parameters);

    OutputStream from(OutputStream out, Map<String, Object> parameters);

}
