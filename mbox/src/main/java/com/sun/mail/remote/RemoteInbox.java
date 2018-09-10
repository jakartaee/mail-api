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

package com.sun.mail.remote;

import javax.mail.*;
import com.sun.mail.mbox.*;

/**
 * A remote Inbox folder.  The data is actually managed by our subclass
 * (<code>MboxFolder</code>).  We fetch data from the remote Inbox and
 * add it to the local Inbox.
 *
 * @author Bill Shannon
 */

public class RemoteInbox extends MboxFolder {

    private RemoteStore mstore;

    protected RemoteInbox(RemoteStore store, String name) {
	super(store, name);
	this.mstore = store;
    }

    /**
     * Poll the remote store for any new messages.
     */
    public synchronized boolean hasNewMessages() {
	try {
	    mstore.updateInbox();
	} catch (MessagingException ex) {
	    // ignore it
	}
	return super.hasNewMessages();
    }

    /**
     * Open the folder in the specified mode.
     * Poll the remote store for any new messages first.
     */
    public synchronized void open(int mode) throws MessagingException {
	mstore.updateInbox();
	super.open(mode);
    }

    /**
     * Return the number of messages in this folder.
     * Poll the remote store for any new messages first.
     */
    public synchronized int getMessageCount() throws MessagingException {
	mstore.updateInbox();
	return super.getMessageCount();
    }
}
