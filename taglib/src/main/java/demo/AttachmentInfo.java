/*
 * Copyright (c) 2001, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package demo;

import java.io.*;
import java.util.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 * Used to store attachment information.
 */
public class AttachmentInfo {
    private Part part;
    private int num;


    /**
     * Returns the attachment's content type.
     */
    public String getAttachmentType() throws MessagingException {
	String contentType;
	if ((contentType = part.getContentType()) == null)
	    return "invalid part";
	else
	    return contentType;
    }

    /**
     * Returns the attachment's content (if it is plain text).
     */
    public String getContent() throws IOException, MessagingException {
	if (hasMimeType("text/plain"))
	    return (String)part.getContent();
	else
	    return "";
    }

    /**
     * Returns the attachment's description.
     */
    public String getDescription() throws MessagingException {
	String description;
	if ((description = part.getDescription()) != null)
	    return description;
	else
	    return "";
    }

    /**
     * Returns the attachment's filename.
     */
    public String getFilename() throws MessagingException {
	String filename;
	if ((filename = part.getFileName()) != null)
	    return filename;
	else
	    return "";
    }

    /**
     * Returns the attachment number.
     */
    public String getNum() {
	return (Integer.toString(num));
    }

    /**
     * Method for checking if the attachment has a description.
     */
    public boolean hasDescription() throws MessagingException {
	return (part.getDescription() != null);
    }

    /**
     * Method for checking if the attachment has a filename.
     */
    public boolean hasFilename() throws MessagingException {
	return (part.getFileName() != null);
    }

    /**
     * Method for checking if the attachment has the desired mime type.
     */
    public boolean hasMimeType(String mimeType) throws MessagingException {
	return part.isMimeType(mimeType);
    }

    /**
     * Method for checking the content disposition.
     */
    public boolean isInline() throws MessagingException {
	if (part.getDisposition() != null)
	    return part.getDisposition().equals(Part.INLINE);
	else
	    return true;
    }

    /**
     * Method for mapping a message part to this AttachmentInfo class.
     */
    public void setPart(int num, Part part)
	throws MessagingException, ParseException {

	this.part = part;
	this.num = num;
    }
}
