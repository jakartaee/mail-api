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
import javax.mail.MessagingException;
import javax.mail.internet.*;


/**
 * DataContentHandler for multipart/report MIME type.
 * Applications should not use this class directly, it's used indirectly
 * through the JavaBeans Activation Framework.
 *
 * @since	JavaMail 1.4
 */
public class multipart_report implements DataContentHandler {
    private ActivationDataFlavor myDF = new ActivationDataFlavor(
	    MultipartReport.class,
	    "multipart/report", 
	    "Multipart Report");

    /**
     * Return the DataFlavors for this <code>DataContentHandler</code>.
     *
     * @return The DataFlavors
     */
    public DataFlavor[] getTransferDataFlavors() { // throws Exception;
	return new DataFlavor[] { myDF };
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
	else
	    return null;
    }
    
    /**
     * Return the content.
     */
    public Object getContent(DataSource ds) throws IOException {
	try {
	    return new MultipartReport(ds); 
	} catch (MessagingException e) {
	    IOException ioex =
		new IOException("Exception while constructing MultipartReport");
	    ioex.initCause(e);
	    throw ioex;
	}
    }
    
    /**
     * Write the object to the output stream, using the specific MIME type.
     */
    public void writeTo(Object obj, String mimeType, OutputStream os) 
			throws IOException {
	if (obj instanceof MultipartReport) {
	    try {
		((MultipartReport)obj).writeTo(os);
	    } catch (MessagingException e) {
		throw new IOException(e.toString());
	    }
	}
    }
}
