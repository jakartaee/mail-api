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

package com.sun.mail.gimap;

import java.io.IOException;

import javax.mail.*;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.protocol.IMAPProtocol;
import com.sun.mail.imap.protocol.ListInfo;
import com.sun.mail.gimap.protocol.GmailProtocol;

/**
 * A Gmail Store.  Defaults to imap.gmail.com with SSL.
 * Uses a GmailProtocol and Gmail Folder to support Gmail extensions.
 *
 * @since JavaMail 1.4.6
 * @author Bill Shannon
 */

public class GmailStore extends IMAPStore {
    /**
     * Constructor that takes a Session object and a URLName that
     * represents a specific IMAP server.
     *
     * @param	session	the Session
     * @param	url	the URLName of this store
     */
    public GmailStore(Session session, URLName url) {
	this(session, url, "gimap", true);
    }

    /**
     * Constructor used by GmailSSLStore subclass.
     *
     * @param	session	the Session
     * @param	url	the URLName of this store
     * @param	name	the protocol name
     * @param	isSSL	use SSL to connect?
     */
    protected GmailStore(Session session, URLName url,
                                String name, boolean isSSL) {
	super(session, url, name, true);	// Gmail requires SSL
    }

    protected boolean protocolConnect(String host, int pport,
				String user, String password)
				throws MessagingException {
	if (host == null)
	    host = "imap.gmail.com";		// default to Gmail host
	return super.protocolConnect(host, pport, user, password);
    }

    protected IMAPProtocol newIMAPProtocol(String host, int port)
				throws IOException, ProtocolException {
	return new GmailProtocol(name, host, port, 
					    session.getProperties(),
					    isSSL,
					    logger
					   );
    }

    /**
     * Create an IMAPFolder object.
     */
    protected IMAPFolder newIMAPFolder(String fullName, char separator,
				Boolean isNamespace) {
	return new GmailFolder(fullName, separator, this, isNamespace);
    }

    /**
     * Create an IMAPFolder object.
     */
    protected IMAPFolder newIMAPFolder(ListInfo li) {
	return new GmailFolder(li, this);
    }
}
