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

import java.util.*;
import java.net.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.*;

/**
 * Custom tag for sending messages.
 */
public class SendTag extends BodyTagSupport {
    private String body;
    private String cc;
    private String host;
    private String recipients;
    private String sender;
    private String subject;

    /**
     * host attribute setter method.
     */
    public void setHost(String host) {
	this.host = host;
    }
    
    /**
     * recipient attribute setter method.
     */
    public void setRecipients(String recipients) {
	this.recipients = recipients;
    }

    /**
     * sender attribute setter method.
     */
    public void setSender(String sender) {
	this.sender = sender;
    }

    /**
     * cc attribute setter method.
     */
    public void setCc(String cc) {
	this.cc = cc;
    }

    /**
     * subject attribute setter method.
     */
    public void setSubject(String subject) {
	this.subject = subject;
    }

    /**
     * Method for processing the end of the tag.
     */
    public int doEndTag() throws JspException {
	Properties props = System.getProperties();
	
	try {
	    if (host != null)
		props.put("mail.smtp.host", host);
	    else if (props.getProperty("mail.smtp.host") == null)
		props.put("mail.smtp.host", InetAddress.getLocalHost().
		    getHostName());
	} catch (Exception ex) {
	    throw new JspException(ex.getMessage());
	}
	Session session = Session.getDefaultInstance(props, null);
		
	Message msg = new MimeMessage(session);
	InternetAddress[] toAddrs = null, ccAddrs = null;

	try {
	    if (recipients != null) {
		toAddrs = InternetAddress.parse(recipients, false);
		msg.setRecipients(Message.RecipientType.TO, toAddrs);
	    } else
		throw new JspException("No recipient address specified");

	    if (sender != null)
		msg.setFrom(new InternetAddress(sender));
	    else
		throw new JspException("No sender address specified");

	    if (cc != null) {
		ccAddrs = InternetAddress.parse(cc, false);
		msg.setRecipients(Message.RecipientType.CC, ccAddrs);
	    }

	    if (subject != null)
		msg.setSubject(subject);

	    if ((body = getBodyContent().getString()) != null)
		msg.setText(body);
	    else
		msg.setText("");

	    Transport.send(msg);
	
	} catch (Exception ex) {
	    throw new JspException(ex.getMessage());
	}

	return(EVAL_PAGE);
   }
}

