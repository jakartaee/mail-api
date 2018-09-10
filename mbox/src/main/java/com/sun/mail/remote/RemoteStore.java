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

import java.io.*;
import javax.mail.*;
import com.sun.mail.mbox.*;

/**
 * A wrapper around a local <code>MboxStore</code> that fetches data
 * from the Inbox in a remote store and adds it to our local Inbox.
 */
public abstract class RemoteStore extends MboxStore {

    protected Store remoteStore;
    protected Folder remoteInbox;
    protected Folder inbox;
    protected String host, user, password;
    protected int port;
    protected long lastUpdate = 0;

    public RemoteStore(Session session, URLName url) {
	super(session, url);
	remoteStore = getRemoteStore(session, url);
    }

    /**
     * Subclasses override this method to return the appropriate
     * <code>Store</code> object.  This method will be called by
     * the <code>RemoteStore</code> constructor.
     */
    protected abstract Store getRemoteStore(Session session, URLName url);

    /**
     * Connect to the store.
     */
    public void connect(String host, int port, String user, String password)
			throws MessagingException {
	this.host = host;
	this.port = port;
	this.user = user;
	this.password = password;
	updateInbox();
    }

    /**
     * Fetch any new mail in the remote INBOX and add it to the local INBOX.
     */
    protected void updateInbox() throws MessagingException {
	// is it time to do an update yet?
	// XXX - polling frequency, rules, etc. should be in properties
	if (System.currentTimeMillis() < lastUpdate + (5 * 1000))
	    return;
	try {
	    /*
	     * Connect to the remote store, using the saved
	     * authentication information.
	     */
	    remoteStore.connect(host, port, user, password);

	    /*
	     * If this store isn't connected yet, do it now, because
	     * it needs to be connected to get the INBOX folder.
	     */
	    if (!isConnected())
		super.connect(host, port, user, password);
	    if (remoteInbox == null)
		remoteInbox = remoteStore.getFolder("INBOX");
	    if (inbox == null)
		inbox = getFolder("INBOX");
	    remoteInbox.open(Folder.READ_WRITE);
	    Message[] msgs = remoteInbox.getMessages();
	    inbox.appendMessages(msgs);
	    remoteInbox.setFlags(msgs, new Flags(Flags.Flag.DELETED), true);
	    remoteInbox.close(true);
	    remoteStore.close();
	} catch (MessagingException ex) {
	    try {
		if (remoteInbox != null && remoteInbox.isOpen())
		    remoteInbox.close(false);
	    } finally {
		if (remoteStore != null && remoteStore.isConnected())
		    remoteStore.close();
	    }
	    throw ex;
	}
    }

    public Folder getDefaultFolder() throws MessagingException {
	checkConnected();

	return new RemoteDefaultFolder(this, null);
    }

    public Folder getFolder(String name) throws MessagingException {
	checkConnected();

	if (name.equalsIgnoreCase("INBOX"))
	    return new RemoteInbox(this, name);
	else
	    return super.getFolder(name);
    }

    public Folder getFolder(URLName url) throws MessagingException {
	checkConnected();
	return getFolder(url.getFile());
    }

    private void checkConnected() throws MessagingException {
	if (!isConnected())
	    throw new MessagingException("Not connected");
    }
}
