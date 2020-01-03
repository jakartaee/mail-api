/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.util.*;
import java.io.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.*;


/**
 * Demo app that shows a very simple Mail Client
 *
 * @author Christopher Cotton
 * @author Bill Shannon
 */

public class SimpleClient {

    static Vector url = new Vector();
    static FolderViewer fv;
    static MessageViewer mv;

    public static void main(String argv[]) {
	boolean usage = false;

	for (int optind = 0; optind < argv.length; optind++) {
	    if (argv[optind].equals("-L")) {
		url.addElement(argv[++optind]);
	    } else if (argv[optind].startsWith("-")) {
		usage = true;
		break;
	    } else {
		usage = true;
		break;
	    }
	}

	if (usage || url.size() == 0) {
	    System.out.println("Usage: SimpleClient -L url");
	    System.out.println("   where url is protocol://username:password@hostname/");
	    System.exit(1);
	}

	try {
	    // Set up our Mailcap entries.  This will allow the JAF
	    // to locate our viewers.
	    File capfile = new File("simple.mailcap");
	    if (!capfile.isFile()) {
		System.out.println(
		    "Cannot locate the \"simple.mailcap\" file.");
		System.exit(1);
	    }
	    
	    CommandMap.setDefaultCommandMap( new MailcapCommandMap(
		new FileInputStream(capfile)));
		
	    JFrame frame = new JFrame("Simple Jakarta Mail Client");
	    frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {System.exit(0);}});
	    //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	    // Get a Store object
	    SimpleAuthenticator auth = new SimpleAuthenticator(frame);
	    Session session = 
		Session.getDefaultInstance(System.getProperties(), auth);
	    //session.setDebug(true);

	    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

	    // create a node for each store we have
	    for (Enumeration e = url.elements() ; e.hasMoreElements() ;) {
		String urlstring = (String) e.nextElement();
		URLName urln = new URLName(urlstring);
		Store store = session.getStore(urln);
		
		StoreTreeNode storenode = new StoreTreeNode(store);
		root.add(storenode);
	    }	    

	    DefaultTreeModel treeModel = new DefaultTreeModel(root);
	    JTree tree = new JTree(treeModel);
	    tree.addTreeSelectionListener(new TreePress());

	    /* Put the Tree in a scroller. */
	    JScrollPane        sp = new JScrollPane();
	    sp.setPreferredSize(new Dimension(250, 300));
	    sp.getViewport().add(tree);

	    /* Create a double buffered JPanel */
	    JPanel sv = new JPanel(new BorderLayout());
	    sv.add("Center", sp);

	    fv = new FolderViewer(null);

	    JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				sv, fv);
	    jsp.setOneTouchExpandable(true);
	    mv = new MessageViewer();
	    JSplitPane jsp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				jsp, mv);
	    jsp2.setOneTouchExpandable(true);

	    frame.getContentPane().add(jsp2);
	    frame.pack();
	    frame.show();

	} catch (Exception ex) {
	    System.out.println("SimpletClient caught exception");
	    ex.printStackTrace();
	    System.exit(1);
	}
    }

}

class TreePress implements TreeSelectionListener {
    
    public void valueChanged(TreeSelectionEvent e) {
	TreePath path = e.getNewLeadSelectionPath();
	if (path != null) {
	    Object o = path.getLastPathComponent();
	    if (o instanceof FolderTreeNode) {
		FolderTreeNode node = (FolderTreeNode)o;
		Folder folder = node.getFolder();

		try {
		    if ((folder.getType() & Folder.HOLDS_MESSAGES) != 0) {
			SimpleClient.fv.setFolder(folder);
		    }
		} catch (MessagingException me) { }
	    }
	}
    }
}
