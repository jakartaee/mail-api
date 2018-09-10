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

package com.sun.mail.gimap.protocol;

import java.io.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.*;

import com.sun.mail.iap.*;
import com.sun.mail.imap.protocol.*;
import com.sun.mail.gimap.*;

/**
 * Support Gmail-specific search extensions.
 *
 * @since JavaMail 1.4.6
 * @author Bill Shannon
 */

public class GmailSearchSequence extends SearchSequence {
    public GmailSearchSequence(IMAPProtocol p) {
	super(p);
    }

    public Argument generateSequence(SearchTerm term, String charset)
				throws SearchException, IOException {
	if (term instanceof GmailMsgIdTerm)
	    return gmailMsgidSearch((GmailMsgIdTerm)term);
	else if (term instanceof GmailThrIdTerm)
	    return gmailThridSearch((GmailThrIdTerm)term);
	else if (term instanceof GmailRawSearchTerm)
	    return gmailRawSearch((GmailRawSearchTerm)term, charset);
	else
	    return super.generateSequence(term, charset);
    }

    protected Argument gmailMsgidSearch(GmailMsgIdTerm term)
				throws IOException {
	Argument result = new Argument();
	result.writeAtom("X-GM-MSGID");
	result.writeNumber(term.getNumber());
	return result;
    }

    protected Argument gmailThridSearch(GmailThrIdTerm term)
				throws IOException {
	Argument result = new Argument();
	result.writeAtom("X-GM-THRID");
	result.writeNumber(term.getNumber());
	return result;
    }

    protected Argument gmailRawSearch(GmailRawSearchTerm term, String charset)
				throws IOException {
	Argument result = new Argument();
	result.writeAtom("X-GM-RAW");
	result.writeString(term.getPattern(), charset);
	return result;
    }
}
