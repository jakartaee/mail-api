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
import jakarta.mail.*;
import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.*;

/**
 * Custom tag for listing message attachments. The scripting variable is only
 * within the body of the tag.
 */
public class ListAttachmentsTag extends BodyTagSupport {
    private String messageinfo;
    private int partNum = 1;
    private int numParts = 0;
    private AttachmentInfo attachmentinfo;
    private MessageInfo messageInfo;
    private Multipart multipart;

    /**
     * messageinfo attribute getter method.
     */
    public String getMessageinfo() {
	return messageinfo;
    }
    
    /**
     * messageinfo attribute setter method.
     */
    public void setMessageinfo(String messageinfo) {
	this.messageinfo = messageinfo;
    }

    /**
     * Method for processing the start of the tag.
     */
    public int doStartTag() throws JspException {
	messageInfo = (MessageInfo)pageContext.getAttribute(getMessageinfo());
	attachmentinfo = new AttachmentInfo();
	
	try {
	    multipart = (Multipart)messageInfo.getMessage().getContent();
	    numParts = multipart.getCount();
	} catch (Exception ex) {
	    throw new JspException(ex.getMessage());
	}

	getPart();

	return BodyTag.EVAL_BODY_TAG;
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
       
	partNum++;
	if (partNum < numParts) {
	    getPart();
	    return BodyTag.EVAL_BODY_TAG;
	} else {
	    return BodyTag.SKIP_BODY;
	}
    }
    
    /**
     * Helper method for retrieving message parts.
     */
    private void getPart() throws JspException {
	try {
	    attachmentinfo.setPart(partNum, multipart.getBodyPart(partNum));
	    pageContext.setAttribute(getId(), attachmentinfo);
	} catch (Exception ex) {
	    throw new JspException(ex.getMessage());
	}
    }
}

