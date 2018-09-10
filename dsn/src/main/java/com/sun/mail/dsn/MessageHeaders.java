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

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * A special MimeMessage object that contains only message headers,
 * no content.  Used to represent the MIME type text/rfc822-headers.
 *
 * @since	JavaMail 1.4
 */
public class MessageHeaders extends MimeMessage {

    /**
     * Construct a MessageHeaders object.
     *
     * @exception	MessagingException for failures
     */
    public MessageHeaders() throws MessagingException {
	super((Session)null);
	content = new byte[0];
    }

    /**
     * Constructs a MessageHeaders object from the given InputStream.
     *
     * @param	is	InputStream
     * @exception	MessagingException for failures
     */
    public MessageHeaders(InputStream is) throws MessagingException {
	super(null, is);
	content = new byte[0];
    }

    /**
     * Constructs a MessageHeaders object using the given InternetHeaders.
     *
     * @param	headers	InternetHeaders to use
     * @exception	MessagingException for failures
     */
    public MessageHeaders(InternetHeaders headers) throws MessagingException {
	super((Session)null);
	this.headers = headers;
	content = new byte[0];
    }

    /**
     * Return the size of this message.
     * Always returns zero.
     */
    public int getSize() {
	return 0;
    }

    public InputStream getInputStream() {
	return new ByteArrayInputStream(content);
    }

    protected InputStream getContentStream() {
	return new ByteArrayInputStream(content);
    }

    /**
     * Can't set any content for a MessageHeaders object.
     *
     * @exception	MessagingException	always
     */
    public void setDataHandler(DataHandler dh) throws MessagingException {
	throw new MessagingException("Can't set content for MessageHeaders");
    }

}
