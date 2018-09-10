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
import java.util.Properties;
import java.awt.datatransfer.DataFlavor;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;


/**
 * DataContentHandler for message/delivery-status MIME type.
 * Applications should not use this class directly, it's used indirectly
 * through the JavaBeans Activation Framework.
 *
 * @since	JavaMail 1.4
 */
public class message_deliverystatus implements DataContentHandler {

    ActivationDataFlavor ourDataFlavor = new ActivationDataFlavor(
	DeliveryStatus.class,
	"message/delivery-status", 
	"Delivery Status");

    /**
     * return the DataFlavors for this <code>DataContentHandler</code>
     * @return The DataFlavors.
     */
    public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[] { ourDataFlavor };
    }

    /**
     * return the Transfer Data of type DataFlavor from InputStream
     * @param df The DataFlavor.
     * @param ds The DataSource corresponding to the data.
     * @return a Message object
     */
    public Object getTransferData(DataFlavor df, DataSource ds)
				throws IOException {
	// make sure we can handle this DataFlavor
	if (ourDataFlavor.equals(df))
	    return getContent(ds);
	else
	    return null;
    }
    
    /**
     * Return the content.
     */
    public Object getContent(DataSource ds) throws IOException {
	// create a new DeliveryStatus
	try {
	    /*
	    Session session;
	    if (ds instanceof MessageAware) {
		javax.mail.MessageContext mc =
			((MessageAware)ds).getMessageContext();
		session = mc.getSession();
	    } else {
		// Hopefully a rare case.  Also hopefully the application
		// has created a default Session that can just be returned
		// here.  If not, the one we create here is better than
		// nothing, but overall not a really good answer.
		session = Session.getDefaultInstance(new Properties(), null);
	    }
	    return new DeliveryStatus(session, ds.getInputStream());
	    */
	    return new DeliveryStatus(ds.getInputStream());
	} catch (MessagingException me) {
	    throw new IOException("Exception creating DeliveryStatus in " +
		    "message/delivery-status DataContentHandler: " +
		    me.toString());
	}
    }
    
    /**
     */
    public void writeTo(Object obj, String mimeType, OutputStream os) 
			throws IOException {
	// if the object is a DeliveryStatus, we know how to write that out
	if (obj instanceof DeliveryStatus) {
	    DeliveryStatus ds = (DeliveryStatus)obj;
	    ds.writeTo(os);
	    
	} else {
	    throw new IOException("unsupported object");
	}
    }
}
