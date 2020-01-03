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
import jakarta.mail.Store;
import jakarta.mail.Folder;
import jakarta.mail.MessagingException;

/**
 * Node which represents a Folder in the jakarta.mail apis. 
 *
 * @author Christopher Cotton
 */
public class FolderTreeNode extends DefaultMutableTreeNode {
    
    protected Folder	folder = null;
    protected boolean	hasLoaded = false;

    /**
     * creates a tree node that points to the particular Store.
     *
     * @param what	the store for this node
     */
    public FolderTreeNode(Folder what) {
	super(what);
	folder = what;
    }

    
    /**
     * a Folder is a leaf if it cannot contain sub folders
     */
    public boolean isLeaf() {
	try {
	    if ((folder.getType() & Folder.HOLDS_FOLDERS) == 0)
		return true;
	} catch (MessagingException me) { }
	
	// otherwise it does hold folders, and therefore not
	// a leaf
	return false;
    }
   
    /**
     * returns the folder for this node
     */
    public Folder getFolder() {
	return folder;
    }
    


    /**
     * return the number of children for this folder node. The first
     * time this method is called we load up all of the folders
     * under the store's defaultFolder
     */

    public int getChildCount() {
	if (!hasLoaded) {
	    loadChildren();
	}
	return super.getChildCount();
    }
    
    protected void loadChildren() {
	// if it is a leaf, just say we have loaded them
	if (isLeaf()) {
	    hasLoaded = true;
	    return;
	}

	try {
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
     * override toString() since we only want to display a folder's
     * name, and not the full path of the folder
     */
    public String toString() {
	return folder.getName();
    }
    
}

