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
import jakarta.mail.search.*;
import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.*;

/**
 * Custom tag for listing messages. The scripting variable is only
 * within the body of the tag.
 */
public class ListMessagesTag extends BodyTagSupport {
    private String folder;
    private String session;
    private int msgNum = 0;
    private int messageCount = 0;
    private Message message;
    private Message[] messages;
    private MessageInfo messageinfo;
    
    /**
     * folder attribute getter method.
     */
    public String getFolder() {
	return folder;
    }
    
    /**
     * session attribute getter method.
     */
    public String getSession() {
	return session;
    }
    
    /**
     * folder setter method.
     */
    public void setFolder(String folder) {
	this.folder = folder;
    }

    /**
     * session attribute setter method.
     */
    public void setSession(String session) {
	this.session = session;
    }

    /**
     * Method for processing the start of the tag.
     */
    public int doStartTag() throws JspException {
	messageinfo = new MessageInfo();
       
	try {
	    Folder folder = (Folder)pageContext.getAttribute(
		getFolder(), PageContext.SESSION_SCOPE);
	    FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.DELETED), false);
	    messages = folder.search(ft);
	    messageCount = messages.length;
	    msgNum = 0;
	} catch (Exception ex) {
	    throw new JspException(ex.getMessage());
	}

	if (messageCount > 0) {
	    getMessage();
	    return BodyTag.EVAL_BODY_TAG;
	} else
	    return BodyTag.SKIP_BODY;
    }
   
    /**
     * Method for processing the body content of the tag.
     */
    public int doAfterBody() throws JspException {
	
	BodyContent body = getBodyContent();
	try {
	    body.writeOut(getPreviousOut());
	} catch (IOException e) {
	    throw new JspTagException("IterationTag: " + e.getMessage());
	}
	
	// clear up so the next time the body content is empty
	body.clearBody();
       
	if (msgNum < messageCount) {
	    getMessage();
	    return BodyTag.EVAL_BODY_TAG;
	} else {
	    return BodyTag.SKIP_BODY;
	}
    }
    
    /**
     * Helper method for retrieving messages.
     */
    private void getMessage() throws JspException {
	message = messages[msgNum++];
	messageinfo.setMessage(message);
	pageContext.setAttribute(getId(), messageinfo);
    }
}

