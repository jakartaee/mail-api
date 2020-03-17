/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import javax.swing.tree.DefaultMutableTreeNode;
import jakarta.mail.*;

/**
 * Node which represents a Store in the jakarta.mail apis. 
 *
 * @author Christopher Cotton
 */
public class StoreTreeNode extends DefaultMutableTreeNode {
    
    protected Store	store = null;
    protected Folder	folder = null;
    protected String	display = null;

    /**
     * creates a tree node that points to the particular Store.
     *
     * @param what	the store for this node
     */
    public StoreTreeNode(Store what) {
	super(what);
	store = what;
    }

    
    /**
     * a Store is never a leaf node.  It can always contain stuff
     */
    public boolean isLeaf() {
	return false;
    }
   

    /**
     * return the number of children for this store node. The first
     * time this method is called we load up all of the folders
     * under the store's defaultFolder
     */

    public int getChildCount() {
	if (folder == null) {
	    loadChildren();
	}
	return super.getChildCount();
    }
    
    protected void loadChildren() {
	try {
	    // connect to the Store if we need to
	    if (!store.isConnected()) {
		store.connect();
	    }

	    // get the default folder, and list the
	    // subscribed folders on it
	    folder = store.getDefaultFolder();
	    // Folder[] sub = folder.listSubscribed();
	    Folder[] sub = folder.list();

	    // add a FolderTreeNode for each Folder
	    int num = sub.length;
	    for(int i = 0; i < num; i++) {
		FolderTreeNode node = new FolderTreeNode(sub[i]);
		// we used insert here, since add() would make
		// another recursive call to getChildCount();
		insert(node, i);
	    }
	    
	} catch (MessagingException me) {
	    me.printStackTrace();
	}
    }

    /**
     * We override toString() so we can display the store URLName
     * without the password.
     */

    public String toString() {
	if (display == null) {
	    URLName url = store.getURLName();
	    if (url == null) {
		display = store.toString();
	    } else {
		// don't show the password
		URLName too = new URLName( url.getProtocol(), url.getHost(), url.getPort(),
					   url.getFile(), url.getUsername(), null);
		display = too.toString();
	    }
	}
	
	return display;
    }
    
    
}

