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

package com.sun.mail.dsn;

import java.io.*;
import java.awt.datatransfer.DataFlavor;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * DataContentHandler for text/rfc822-headers MIME type.
 * Applications should not use this class directly, it's used indirectly
 * through the JavaBeans Activation Framework.
 *
 * @since	JavaMail 1.4
 *
 */
public class text_rfc822headers implements DataContentHandler {
    private static ActivationDataFlavor myDF = new ActivationDataFlavor(
	MessageHeaders.class,
	"text/rfc822-headers",
	"RFC822 headers");
    private static ActivationDataFlavor myDFs = new ActivationDataFlavor(
	java.lang.String.class,
	"text/rfc822-headers",
	"RFC822 headers");

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { myDF, myDFs };
    }

    /**
     * Return the Transfer Data of type DataFlavor from InputStream.
     *
     * @param df The DataFlavor
     * @param ds The DataSource corresponding to the data
     * @return String object
     */
    public Object getTransferData(DataFlavor df, DataSource ds) 
			throws IOException {
	// use myDF.equals to be sure to get ActivationDataFlavor.equals,
	// which properly ignores Content-Type parameters in comparison
	if (myDF.equals(df))
	    return getContent(ds);
	else if (myDFs.equals(df))
	    return getStringContent(ds);
	else
	    return null;
    }

    public Object getContent(DataSource ds) throws IOException {
	try {
	    return new MessageHeaders(ds.getInputStream());
	} catch (MessagingException mex) {
//System.out.println("Exception creating MessageHeaders: " + mex);
	    throw new IOException("Exception creating MessageHeaders: " + mex);
	}
    }

    private Object getStringContent(DataSource ds) throws IOException {
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
    public void writeTo(Object obj, String type, OutputStream os) 
			throws IOException {
	if (obj instanceof MessageHeaders) {
	    MessageHeaders mh = (MessageHeaders)obj;
	    try {
		mh.writeTo(os);
	    } catch (MessagingException mex) {
		Exception ex = mex.getNextException();
		if (ex instanceof IOException)
		    throw (IOException)ex;
		else
		    throw new IOException("Exception writing headers: " + mex);
	    }
	    return;
	}
	if (!(obj instanceof String))
	    throw new IOException("\"" + myDFs.getMimeType() +
		"\" DataContentHandler requires String object, " +
		"was given object of type " + obj.getClass().toString());

	String enc = null;
	OutputStreamWriter osw = null;

	try {
	    enc = getCharset(type);
	    osw = new OutputStreamWriter(os, enc);
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
	osw.flush();
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
