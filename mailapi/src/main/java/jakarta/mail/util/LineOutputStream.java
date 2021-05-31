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

/**
 * This interface is to support writing out Strings as a sequence of bytes
 * terminated by a CRLF sequence. The String must contain only US-ASCII
 * characters.<p>
 *
 * The expected use is to write out RFC822 style headers to an output
 * stream. <p>
 *
 */
public interface LineOutputStream {

	/**
	 * Writes the input string and a new line (CRLF).
	 * @param s the string to write before the new line.
	 * @throws IOException  if an I/O error occurs.
	 */
    void writeln(String s) throws IOException;

    /**
     * Writes a new line (CRLF).
     * @throws IOException  if an I/O error occurs.
     */
    void writeln() throws IOException;

    /**
     * Writes <code>b.length</code> bytes to this output stream.
     * @param content the content to write.
     * @throws IOException  if an I/O error occurs.
     */
    void write(byte[] content) throws IOException;

}
