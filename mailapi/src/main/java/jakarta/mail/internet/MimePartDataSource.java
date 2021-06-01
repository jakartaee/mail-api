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

package jakarta.mail.internet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownServiceException;

import jakarta.activation.DataSource;
import jakarta.mail.FolderClosedException;
import jakarta.mail.MessageAware;
import jakarta.mail.MessageContext;
import jakarta.mail.MessagingException;



/**
 * A utility class that implements a DataSource out of
 * a MimePart. This class is primarily meant for service providers.
 *
 * @see		jakarta.mail.internet.MimePart
 * @see		jakarta.activation.DataSource
 * @author 	John Mani
 */

public class MimePartDataSource implements DataSource, MessageAware {
    /**
     * The MimePart that provides the data for this DataSource.
     *
     * @since	JavaMail 1.4
     */
    protected MimePart part;

    private MessageContext context;

    /**
     * Constructor, that constructs a DataSource from a MimePart.
     *
     * @param	part	the MimePart
     */
    public MimePartDataSource(MimePart part) {
	this.part = part;
    }

    /**
     * Returns an input stream from this  MimePart. <p>
     *
     * This method applies the appropriate transfer-decoding, based 
     * on the Content-Transfer-Encoding attribute of this MimePart.
     * Thus the returned input stream is a decoded stream of bytes.<p>
     *
     * This implementation obtains the raw content from the Part
     * using the <code>getContentStream()</code> method and decodes
     * it using the <code>MimeUtility.decode()</code> method.
     *
     * @see	jakarta.mail.internet.MimeMessage#getContentStream
     * @see	jakarta.mail.internet.MimeBodyPart#getContentStream
     * @see	jakarta.mail.internet.MimeUtility#decode
     * @return 	decoded input stream
     */
    @Override
    public InputStream getInputStream() throws IOException {
	InputStream is;

	try {
	    if (part instanceof MimeBodyPart)
		is = ((MimeBodyPart)part).getContentStream();
	    else if (part instanceof MimeMessage)
		is = ((MimeMessage)part).getContentStream();
	    else
		throw new MessagingException("Unknown part");
	    
	    String encoding =
		MimeBodyPart.restrictEncoding(part, part.getEncoding());
	    if (encoding != null)
		return MimeUtility.decode(is, encoding);
	    else
		return is;
	} catch (FolderClosedException fex) {
		throw new IOException(new FolderClosedException(fex.getFolder(), fex.getMessage(), fex));
	} catch (MessagingException mex) {
		// Cause is removed because in upper stack the cause is checked. See test POP3FolderClosedExceptionTest
	    throw new IOException(mex.getMessage());
	}
    }

    /**
     * DataSource method to return an output stream. <p>
     *
     * This implementation throws the UnknownServiceException.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
	throw new UnknownServiceException("Writing not supported");
    }

    /**
     * Returns the content-type of this DataSource. <p>
     *
     * This implementation just invokes the <code>getContentType</code>
     * method on the MimePart.
     */
    @Override
    public String getContentType() {
	try {
	    return part.getContentType();
	} catch (MessagingException mex) {
	    // would like to be able to reflect the exception to the
	    // application, but since we can't do that we return a
	    // generic "unknown" value here and hope for another
	    // exception later.
	    return "application/octet-stream";
	}
    }

    /**
     * DataSource method to return a name.  <p>
     *
     * This implementation just returns an empty string.
     */
    @Override
    public String getName() {
	try {
	    if (part instanceof MimeBodyPart)
		return ((MimeBodyPart)part).getFileName();
	} catch (MessagingException mex) {
	    // ignore it
	}
	return "";
    }

    /**
     * Return the <code>MessageContext</code> for the current part.
     * @since JavaMail 1.1
     */
    @Override
    public synchronized MessageContext getMessageContext() {
	if (context == null)
	    context = new MessageContext(part);
	return context;
    }
}
