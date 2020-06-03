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

import jakarta.mail.*;
import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.*;

/**
 * Custom tag for retrieving a message.
 */
public class MessageTag extends TagSupport {
    private String folder;
    private String session;
    private int num = 1;

    /**
     * folder attribute setter method.
     */
    public String getFolder() {
	return folder;
    }
    
    /**
     * num attribute getter method.
     */
    public String getNum() {
	return Integer.toString(num);
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
     * num attribute setter method.
     */
    public void setNum(String num) {
	this.num = Integer.parseInt(num);
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
	MessageInfo messageinfo = new MessageInfo();
	try {
	    Folder f = (Folder)pageContext.getAttribute(
		getFolder(), PageContext.SESSION_SCOPE);
	    Message message = f.getMessage(num);
	    messageinfo.setMessage(message);
	    pageContext.setAttribute(getId(), messageinfo);
	} catch (Exception ex) {
	    throw new JspException(ex.getMessage());
	}
 
	return SKIP_BODY;
   }
}

