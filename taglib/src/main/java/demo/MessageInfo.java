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

import java.text.*;
import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 * Used to store message information.
 */
public class MessageInfo {
    private Message message;
    
    
    /**
     * Returns the bcc field.
     */
    public String getBcc() throws MessagingException {
	return formatAddresses(
	    message.getRecipients(Message.RecipientType.BCC));
    }
    
    /**
     * Returns the body of the message (if it's plain text).
     */
    public String getBody() throws MessagingException, java.io.IOException {
	Object content = message.getContent();
	if (message.isMimeType("text/plain")) {
	    return (String)content;
	} else if (message.isMimeType("multipart/alternative")) {
	    Multipart mp = (Multipart)message.getContent();
	    int numParts = mp.getCount();
	    for (int i = 0; i < numParts; ++i) {
		if (mp.getBodyPart(i).isMimeType("text/plain"))
		    return (String)mp.getBodyPart(i).getContent();
	    }
	    return "";   
	} else if (message.isMimeType("multipart/*")) { 
	    Multipart mp = (Multipart)content;
	    if (mp.getBodyPart(0).isMimeType("text/plain"))
		return (String)mp.getBodyPart(0).getContent();
	    else
		return "";
	} else
	    return "";
    }
    
    /**
     * Returns the cc field.
     */
    public String getCc() throws MessagingException {
	return formatAddresses(
	    message.getRecipients(Message.RecipientType.CC));
    }
    
    /**
     * Returns the date the message was sent (or received if the sent date
     * is null.
     */
    public String getDate() throws MessagingException {
	Date date;
	SimpleDateFormat df = new SimpleDateFormat("EE M/d/yy");
	if ((date = message.getSentDate()) != null)
	    return (df.format(date));
	else if ((date = message.getReceivedDate()) != null)
	    return (df.format(date));
	else
	    return "";
     }
       
    /**
     * Returns the from field.
     */
    public String getFrom() throws MessagingException {
	return formatAddresses(message.getFrom());
    }

    /**
     * Returns the address to reply to.
     */
    public String getReplyTo() throws MessagingException {
	Address[] a = message.getReplyTo();
	if (a.length > 0)
	    return ((InternetAddress)a[0]).getAddress();
	else
	    return "";
    }
    
    /**
     * Returns the jakarta.mail.Message object.
     */
    public Message getMessage() {
	return message;
    }
    
    /**
     * Returns the message number.
     */
    public String getNum() {
	return (Integer.toString(message.getMessageNumber()));
    }
    
    /**
     * Returns the received date field.
     */
    public String getReceivedDate() throws MessagingException {
	if (hasReceivedDate())
	    return (message.getReceivedDate().toString());
	else
	    return "";
    }
    
    /**
     * Returns the sent date field.
     */
    public String getSentDate() throws MessagingException {
	if (hasSentDate())
	    return (message.getSentDate().toString()); 
	else
	    return "";
    }
    
    /**
     * Returns the subject field.
     */
    public String getSubject() throws MessagingException {
	if (hasSubject())
	    return message.getSubject();
	else
	    return "";
    }
    
    /**
     * Returns the to field.
     */
    public String getTo() throws MessagingException {
	return formatAddresses(
	    message.getRecipients(Message.RecipientType.TO));
    }
    
    /**
     * Method for checking if the message has attachments.
     */
    public boolean hasAttachments() throws java.io.IOException, 
					   MessagingException {
	boolean hasAttachments = false;
	if (message.isMimeType("multipart/*")) {
	    Multipart mp = (Multipart)message.getContent();
	    if (mp.getCount() > 1)
		hasAttachments = true;
	}
	    
	return hasAttachments;
    }
    
    /**
     * Method for checking if the message has a bcc field.
     */
    public boolean hasBcc() throws MessagingException {
	return (message.getRecipients(Message.RecipientType.BCC) != null);
    }
    
    /**
     * Method for checking if the message has a cc field.
     */
    public boolean hasCc() throws MessagingException {
	return (message.getRecipients(Message.RecipientType.CC) != null);
    }
    
    /**
     * Method for checking if the message has a date field.
     */
    public boolean hasDate() throws MessagingException {
	return (hasSentDate() || hasReceivedDate());
    }
    
    /**
     * Method for checking if the message has a from field.
     */
    public boolean hasFrom() throws MessagingException {
	return (message.getFrom() != null);
    }
    
    /**
     * Method for checking if the message has the desired mime type.
     */
    public boolean hasMimeType(String mimeType) throws MessagingException {
	return message.isMimeType(mimeType);
    }
    
    /**
     * Method for checking if the message has a received date field.
     */
    public boolean hasReceivedDate() throws MessagingException {
	return (message.getReceivedDate() != null);
    }
    
    /**
     * Method for checking if the message has a sent date field.
     */
    public boolean hasSentDate() throws MessagingException {
	return (message.getSentDate() != null);
    }
    
    /**
     * Method for checking if the message has a subject field.
     */
    public boolean hasSubject() throws MessagingException {
	return (message.getSubject() != null);
    }
    
    /**
     * Method for checking if the message has a to field.
     */
    public boolean hasTo() throws MessagingException {
	return (message.getRecipients(Message.RecipientType.TO) != null);
    }
    
    /**
     * Method for mapping a message to this MessageInfo class.
     */
    public void setMessage(Message message) {
	this.message = message;
    }

    /**
     * Utility method for formatting msg header addresses.
     */
    private String formatAddresses(Address[] addrs) {
	if (addrs == null)
	    return "";
	StringBuilder strBuf = new StringBuilder(getDisplayAddress(addrs[0]));
	for (int i = 1; i < addrs.length; i++) {
	    strBuf.append(", ").append(getDisplayAddress(addrs[i]));
	}
	return strBuf.toString();
    }
    
    /**
     * Utility method which returns a string suitable for msg header display.
     */
    private String getDisplayAddress(Address a) {
	String pers = null;
	String addr = null;
	if (a instanceof InternetAddress &&
	   ((pers = ((InternetAddress)a).getPersonal()) != null)) {
	    addr = pers + "  "+"&lt;"+((InternetAddress)a).getAddress()+"&gt;";
	} else 
	    addr = a.toString();
	return addr;
    }
}

