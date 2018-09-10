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

import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.util.LineOutputStream;	// XXX
import com.sun.mail.util.PropUtil;
import com.sun.mail.util.MailLogger;

/**
 * A message/disposition-notification message content, as defined in
 * <A HREF="http://www.ietf.org/rfc/rfc3798.txt" TARGET="_top">RFC 3798</A>.
 *
 * @since	JavaMail 1.4.2
 */
public class DispositionNotification extends Report {

    private static MailLogger logger = new MailLogger(
	DeliveryStatus.class,
	"DEBUG DSN",
	PropUtil.getBooleanSystemProperty("mail.dsn.debug", false),
	System.out);

    /**
     * The disposition notification content fields.
     */
    protected InternetHeaders notifications;

    /**
     * Construct a disposition notification with no content.
     *
     * @exception	MessagingException for failures
     */
    public DispositionNotification() throws MessagingException {
	super("disposition-notification");
	notifications = new InternetHeaders();
    }

    /**
     * Construct a disposition notification by parsing the
     * supplied input stream.
     *
     * @param	is	the input stream
     * @exception	IOException for I/O errors reading the stream
     * @exception	MessagingException for other failures
     */
    public DispositionNotification(InputStream is)
				throws MessagingException, IOException {
	super("disposition-notification");
	notifications = new InternetHeaders(is);
	logger.fine("got MDN notification content");
    }

    /**
     * Return all the disposition notification fields in the
     * disposition notification.
     * The fields are defined as:
     *
     * <pre>
     *    disposition-notification-content =
     *		[ reporting-ua-field CRLF ]
     *		[ mdn-gateway-field CRLF ]
     *		[ original-recipient-field CRLF ]
     *		final-recipient-field CRLF
     *		[ original-message-id-field CRLF ]
     *		disposition-field CRLF
     *		*( failure-field CRLF )
     *		*( error-field CRLF )
     *		*( warning-field CRLF )
     *		*( extension-field CRLF )
     * </pre>
     *
     * @return	the DSN fields
     */
    // XXX - could parse each of these fields
    public InternetHeaders getNotifications() {
	return notifications;
    }

    /**
     * Set the disposition notification fields in the
     * disposition notification.
     *
     * @param	notifications	the DSN fields
     */
    public void setNotifications(InternetHeaders notifications) {
	this.notifications = notifications;
    }

    public void writeTo(OutputStream os) throws IOException {
	// see if we already have a LOS
	LineOutputStream los = null;
	if (os instanceof LineOutputStream) {
	    los = (LineOutputStream) os;
	} else {
	    los = new LineOutputStream(os);
	}

	writeInternetHeaders(notifications, los);
	los.writeln();
    }

    private static void writeInternetHeaders(InternetHeaders h,
				LineOutputStream los) throws IOException {
	Enumeration e = h.getAllHeaderLines();
	while (e.hasMoreElements())
	    los.writeln((String)e.nextElement());
    }

    public String toString() {
	return "DispositionNotification: Reporting-UA=" +
	    notifications.getHeader("Reporting-UA", null);
    }
}
