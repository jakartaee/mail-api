/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.io.*;

/**
 * Convert the various newline conventions to the local platform's
 * newline convention. <p>
 *
 * This stream can be used with the Message.writeTo method to
 * generate a message that uses the local plaform's line terminator
 * for the purpose of (e.g.) saving the message to a local file.
 */
public class NewlineOutputStream extends FilterOutputStream {
    private int lastb = -1;
    private static byte[] newline;

    public NewlineOutputStream(OutputStream os) {
	super(os);
	if (newline == null) {
	    String s = System.lineSeparator();
	    if (s == null || s.length() <= 0)
		s = "\n";
	    try {
		newline = s.getBytes("iso-8859-1");	// really us-ascii
	    } catch (UnsupportedEncodingException ex) {
		// should never happen
		newline = new byte[] { (byte)'\n' };
	    }
	}
    }

    public void write(int b) throws IOException {
	if (b == '\r') {
	    out.write(newline);
	} else if (b == '\n') {
	    if (lastb != '\r')
		out.write(newline);
	} else {
	    out.write(b);
	}
	lastb = b;
    }

    public void write(byte b[]) throws IOException {
	write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
	for (int i = 0 ; i < len ; i++) {
	    write(b[off + i]);
	}
    }
}
