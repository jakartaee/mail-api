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

import jakarta.servlet.jsp.*;
import jakarta.servlet.jsp.tagext.*;

/**
 * Extra information class to support the scripting variable created by the
 * MessagesTag class. The variable exists outside of the tag.
 * 
 */
public class MessageTEI extends TagExtraInfo {
    
    public MessageTEI() {
	super();
    }
    
    public VariableInfo[] getVariableInfo(TagData data) {
	VariableInfo info = new VariableInfo(data.getId(),"MessageInfo",
	    true, VariableInfo.AT_END);
	VariableInfo[] varInfo = { info };
	return varInfo;
    }
}

