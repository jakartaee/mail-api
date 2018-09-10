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
import java.util.*;
import java.util.logging.Level;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.util.LineOutputStream;	// XXX
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.MailLogger;

/**
 * A message/delivery-status message content, as defined in
 * <A HREF="http://www.ietf.org/rfc/rfc3464.txt" TARGET="_top">RFC 3464</A>.
 *
 * @since	JavaMail 1.4
 */
public class DeliveryStatus extends Report {

    private static MailLogger logger = new MailLogger(
	DeliveryStatus.class,
	"DEBUG DSN",
	PropUtil.getBooleanSystemProperty("mail.dsn.debug", false),
	System.out);

    /**
     * The DSN fields for the message.
     */
    protected InternetHeaders messageDSN;

    /**
     * The DSN fields for each recipient.
     */
    protected InternetHeaders[] recipientDSN;

    /**
     * Construct a delivery status notification with no content.
     *
     * @exception	MessagingException for failures
     */
    public DeliveryStatus() throws MessagingException {
	super("delivery-status");
	messageDSN = new InternetHeaders();
	recipientDSN = new InternetHeaders[0];
    }

    /**
     * Construct a delivery status notification by parsing the
     * supplied input stream.
     *
     * @param	is	the input stream
     * @exception	IOException for I/O errors reading the stream
     * @exception	MessagingException for other failures
     */
    public DeliveryStatus(InputStream is)
				throws MessagingException, IOException {
	super("delivery-status");
	messageDSN = new InternetHeaders(is);
	logger.fine("got messageDSN");
	Vector<InternetHeaders> v = new Vector<>();
	try {
	    while (is.available() > 0) {
		InternetHeaders h = new InternetHeaders(is);
		logger.fine("got recipientDSN");
		v.addElement(h);
	    }
	} catch (EOFException ex) {
	    logger.log(Level.FINE, "got EOFException", ex);
	}
	if (logger.isLoggable(Level.FINE))
	    logger.fine("recipientDSN size " + v.size());
	recipientDSN = new InternetHeaders[v.size()];
	v.copyInto(recipientDSN);
    }

    /**
     * Return all the per-message fields in the delivery status notification.
     * The fields are defined as:
     *
     * <pre>
     *    per-message-fields =
     *          [ original-envelope-id-field CRLF ]
     *          reporting-mta-field CRLF
     *          [ dsn-gateway-field CRLF ]
     *          [ received-from-mta-field CRLF ]
     *          [ arrival-date-field CRLF ]
     *          *( extension-field CRLF )
     * </pre>
     *
     * @return	the per-message DSN fields
     */
    // XXX - could parse each of these fields
    public InternetHeaders getMessageDSN() {
	return messageDSN;
    }

    /**
     * Set the per-message fields in the delivery status notification.
     *
     * @param	messageDSN	the per-message DSN fields
     */
    public void setMessageDSN(InternetHeaders messageDSN) {
	this.messageDSN = messageDSN;
    }

    /**
     * Return the number of recipients for which we have
     * per-recipient delivery status notification information.
     *
     * @return	the number of recipients
     */
    public int getRecipientDSNCount() {
	return recipientDSN.length;
    }

    /**
     * Return the delivery status notification information for
     * the specified recipient.
     *
     * @param	n	the recipient number
     * @return	the DSN fields for the recipient
     */
    public InternetHeaders getRecipientDSN(int n) {
	return recipientDSN[n];
    }

    /**
     * Add deliver status notification information for another
     * recipient.
     *
     * @param	h	the DSN fields for the recipient
     */
    public void addRecipientDSN(InternetHeaders h) {
	InternetHeaders[] rh = new InternetHeaders[recipientDSN.length + 1];
	System.arraycopy(recipientDSN, 0, rh, 0, recipientDSN.length);
	recipientDSN = rh;
	recipientDSN[recipientDSN.length - 1] = h;
    }

    public void writeTo(OutputStream os) throws IOException {
	// see if we already have a LOS
	LineOutputStream los = null;
	if (os instanceof LineOutputStream) {
	    los = (LineOutputStream) os;
	} else {
	    los = new LineOutputStream(os);
	}

	writeInternetHeaders(messageDSN, los);
	los.writeln();
	for (int i = 0; i < recipientDSN.length; i++) {
	    writeInternetHeaders(recipientDSN[i], los);
	    los.writeln();
	}
    }

    private static void writeInternetHeaders(InternetHeaders h,
				LineOutputStream los) throws IOException {
	Enumeration e = h.getAllHeaderLines();
	while (e.hasMoreElements())
	    los.writeln((String)e.nextElement());
    }

    public String toString() {
	return "DeliveryStatus: Reporting-MTA=" +
	    messageDSN.getHeader("Reporting-MTA", null) + ", #Recipients=" +
	    recipientDSN.length;
    }
}
