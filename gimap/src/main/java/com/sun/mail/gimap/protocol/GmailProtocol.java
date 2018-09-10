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
import java.util.*;
import java.nio.charset.StandardCharsets;

import com.sun.mail.iap.*;
import com.sun.mail.imap.protocol.*;
import com.sun.mail.gimap.GmailFolder.FetchProfileItem;

import com.sun.mail.util.MailLogger;

/**
 * Extend IMAP support to handle Gmail-specific protocol extensions.
 *
 * @since JavaMail 1.4.6
 * @author Bill Shannon
 */

public class GmailProtocol extends IMAPProtocol {
    
    /*
     * Define the Gmail-specific FETCH items.
     */
    public static final FetchItem MSGID_ITEM =
	new FetchItem("X-GM-MSGID", FetchProfileItem.MSGID) {
	    public Object parseItem(FetchResponse r) {
		return Long.valueOf(r.readLong());
	    }
	};
    public static final FetchItem THRID_ITEM =
	new FetchItem("X-GM-THRID", FetchProfileItem.THRID) {
	    public Object parseItem(FetchResponse r) {
		return Long.valueOf(r.readLong());
	    }
	};
    public static final FetchItem LABELS_ITEM =
	new FetchItem("X-GM-LABELS", FetchProfileItem.LABELS) {
	    public Object parseItem(FetchResponse r) {
		return r.readAtomStringList();
	    }
	};

    private static final FetchItem[] myFetchItems = {
	MSGID_ITEM,
	THRID_ITEM,
	LABELS_ITEM
    };

    private FetchItem[] fetchItems = null;

    /**
     * Connect to Gmail.
     *
     * @param name	the protocol name
     * @param host	host to connect to
     * @param port	portnumber to connect to
     * @param props	Properties object used by this protocol
     * @param isSSL	use SSL?
     * @param logger	for log messages
     * @exception	IOException	for I/O errors
     * @exception	ProtocolException	for protocol failures
     */
    public GmailProtocol(String name, String host, int port, 
			Properties props, boolean isSSL, MailLogger logger)
			throws IOException, ProtocolException {
	super(name, host, port, props, isSSL, logger);

	// check to see if this is really Gmail
	if (!hasCapability("X-GM-EXT-1")) {
	    logger.fine("WARNING! Not connected to Gmail!");
	    // XXX - could call "disconnect()" here and make this a fatal error
	} else {
	    logger.fine("connected to Gmail");
	}
    }

    /**
     * Return the additional fetch items supported by the Gmail protocol.
     * Combines our fetch items with those supported by the superclass.
     */
    public FetchItem[] getFetchItems() {
	if (fetchItems != null)
	    return fetchItems;
	FetchItem[] sfi = super.getFetchItems();
	if (sfi == null || sfi.length == 0)
	    fetchItems = myFetchItems;
	else {
	    fetchItems = new FetchItem[sfi.length + myFetchItems.length];
	    System.arraycopy(sfi, 0, fetchItems, 0, sfi.length);
	    System.arraycopy(myFetchItems, 0, fetchItems, sfi.length,
							myFetchItems.length);
	}
	return fetchItems;
    }

    /**
     * Set the specified labels on this message.
     *
     * @param	msgsets	the message sets
     * @param	labels	the labels
     * @param	set	true to set, false to clear
     * @exception	ProtocolException	for protocol failures
     * @since	JavaMail 1.5.5
     */
    public void storeLabels(MessageSet[] msgsets, String[] labels, boolean set)
			throws ProtocolException {
	storeLabels(MessageSet.toString(msgsets), labels, set);
    }

    /**
     * Set the specified labels on this message.
     *
     * @param	start	the first message number
     * @param	end	the last message number
     * @param	labels	the labels
     * @param	set	true to set, false to clear
     * @exception	ProtocolException	for protocol failures
     * @since	JavaMail 1.5.5
     */
    public void storeLabels(int start, int end, String[] labels, boolean set)
			throws ProtocolException {
	storeLabels(String.valueOf(start) + ":" + String.valueOf(end),
		   labels, set);
    }

    /**
     * Set the specified labels on this message.
     *
     * @param	msg	the message number
     * @param	labels	the labels
     * @param	set	true to set, false to clear
     * @exception	ProtocolException	for protocol failures
     * @since	JavaMail 1.5.5
     */
    public void storeLabels(int msg, String[] labels, boolean set)
			throws ProtocolException { 
	storeLabels(String.valueOf(msg), labels, set);
    }

    private void storeLabels(String msgset, String[] labels, boolean set)
			throws ProtocolException {
	Response[] r;
	if (set)
	    r = command("STORE " + msgset + " +X-GM-LABELS",
			 createLabelList(labels));
	else
	    r = command("STORE " + msgset + " -X-GM-LABELS",
			createLabelList(labels));
	
	// Dispatch untagged responses
	notifyResponseHandlers(r);
	handleResult(r[r.length-1]);
    }

    // XXX - assume Gmail always supports UTF-8
    private Argument createLabelList(String[] labels) {
	Argument args = new Argument();	
	Argument itemArgs = new Argument();
	for (int i = 0, len = labels.length; i < len; i++)
	    itemArgs.writeString(labels[i], StandardCharsets.UTF_8);
	args.writeArgument(itemArgs);
	return args;
    }

    /**
     * Return a GmailSearchSequence.
     */
    protected SearchSequence getSearchSequence() {
	if (searchSequence == null)
	    searchSequence = new GmailSearchSequence(this);
	return searchSequence;
    }
}
