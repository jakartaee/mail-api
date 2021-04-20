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

package com.sun.mail.handlers;

import jakarta.activation.ActivationDataFlavor;
import jakarta.activation.DataSource;
import jakarta.mail.internet.ContentType;
import jakarta.mail.internet.MimeUtility;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * DataContentHandler for text/plain.
 *
 */
public class text_plain extends handler_base {
    private static ActivationDataFlavor[] myDF = {
	new ActivationDataFlavor(String.class, "text/plain", "Text String")
    };

    /**
     * Creates a default {@code text_plain}.
     */
    public text_plain() {
    }

    /**
     * An OuputStream wrapper that doesn't close the underlying stream.
     */
    private static class NoCloseOutputStream extends FilterOutputStream {
	NoCloseOutputStream(OutputStream os) {
	    super(os);
	}

	@Override
	public void close() {
	    // do nothing
	}
    }

    @Override
    protected ActivationDataFlavor[] getDataFlavors() {
	return myDF;
    }

    @Override
    public Object getContent(DataSource ds) throws IOException {
	String enc = null;
	InputStreamReader is = null;

	try {
	    enc = getCharset(ds.getContentType());
	    is = new InputStreamReader(ds.getInputStream(), enc);
	} catch (IllegalArgumentException iex) {
	    /*
	     * An unknown charset of the form ISO-XXX-XXX will cause
	     * the JDK to throw an IllegalArgumentException.  The
	     * JDK will attempt to create a classname using this string,
	     * but valid classnames must not contain the character '-',
	     * and this results in an IllegalArgumentException, rather than
	     * the expected UnsupportedEncodingException.  Yikes.
	     */
	    throw new UnsupportedEncodingException(enc);
	}

	try {
	    int pos = 0;
	    int count;
	    char buf[] = new char[1024];

	    while ((count = is.read(buf, pos, buf.length - pos)) != -1) {
		pos += count;
		if (pos >= buf.length) {
		    int size = buf.length;
		    if (size < 256*1024)
			size += size;
		    else
			size += 256*1024;
		    char tbuf[] = new char[size];
		    System.arraycopy(buf, 0, tbuf, 0, pos);
		    buf = tbuf;
		}
	    }
	    return new String(buf, 0, pos);
	} finally {
	    try {
		is.close();
	    } catch (IOException ex) {
		// ignore it
	    }
	}
    }

    /**
     * Write the object to the output stream, using the specified MIME type.
     */
    @Override
    public void writeTo(Object obj, String type, OutputStream os)
			throws IOException {
	if (!(obj instanceof String))
	    throw new IOException("\"" + getDataFlavors()[0].getMimeType() +
		"\" DataContentHandler requires String object, " +
		"was given object of type " + obj.getClass().toString());

	String enc = null;
	OutputStreamWriter osw = null;

	try {
	    enc = getCharset(type);
	    osw = new OutputStreamWriter(new NoCloseOutputStream(os), enc);
	} catch (IllegalArgumentException iex) {
	    /*
	     * An unknown charset of the form ISO-XXX-XXX will cause
	     * the JDK to throw an IllegalArgumentException.  The
	     * JDK will attempt to create a classname using this string,
	     * but valid classnames must not contain the character '-',
	     * and this results in an IllegalArgumentException, rather than
	     * the expected UnsupportedEncodingException.  Yikes.
	     */
	    throw new UnsupportedEncodingException(enc);
	}

	String s = (String)obj;
	osw.write(s, 0, s.length());
	/*
	 * Have to call osw.close() instead of osw.flush() because
	 * some charset converts, such as the iso-2022-jp converter,
	 * don't output the "shift out" sequence unless they're closed.
	 * The NoCloseOutputStream wrapper prevents the underlying
	 * stream from being closed.
	 */
	osw.close();
    }

    private String getCharset(String type) {
	try {
	    ContentType ct = new ContentType(type);
	    String charset = ct.getParameter("charset");
	    if (charset == null)
		// If the charset parameter is absent, use US-ASCII.
		charset = "us-ascii";
	    return MimeUtility.javaCharset(charset);
	} catch (Exception ex) {
	    return null;
	}
    }
}
