/*
 * Copyright (c) 1996, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 *
 * @author	Christopher Cotton
 */

import jakarta.mail.*;

/**
 * copier will copy a specified number of messages from one folder
 * to another folder. it demonstrates how to use the Jakarta Mail APIs
 * to copy messages.<p>
 *
 * Usage for copier: copier <i>store urlname</i> 
 * <i>src folder</i> <i>dest folder</i> <i>start msg #</i> <i>end msg #</i><p>
 *
 */

public class copier {

    public static void main(String argv[]) {
	boolean debug = false;	// change to get more errors
	
	if (argv.length != 5) {
	    System.out.println( "usage: copier <urlname> <src folder>" +
				"<dest folder> <start msg #> <end msg #>");
	    return;
	}

	try {
	    URLName url = new URLName(argv[0]);
	    String src = argv[1];	// source folder
	    String dest = argv[2];	// dest folder
	    int start = Integer.parseInt(argv[3]);	// copy from message #
	    int end = Integer.parseInt(argv[4]);	// to message #

	    // Get the default Session object

	    Session session = Session.getInstance(System.getProperties(), null);
	    // session.setDebug(debug);

	    // Get a Store object that implements the protocol.
	    Store store = session.getStore(url);
	    store.connect();
	    System.out.println("Connected...");

	    // Open Source Folder
	    Folder folder = store.getFolder(src);
	    folder.open(Folder.READ_WRITE);
	    System.out.println("Opened source...");	  

	    if (folder.getMessageCount() == 0) {
		  System.out.println("Source folder has no messages ..");
		  folder.close(false);
		  store.close();
	    }

	    // Open destination folder, create if needed 
	    Folder dfolder = store.getFolder(dest);
	    if (!dfolder.exists()) // create
		dfolder.create(Folder.HOLDS_MESSAGES);

	    Message[] msgs = folder.getMessages(start, end);
	    System.out.println("Got messages...");	  

	    // Copy messages into destination, 
	    folder.copyMessages(msgs, dfolder);
	    System.out.println("Copied messages...");	  

	    // Close the folder and store
	    folder.close(false);
	    store.close();
	    System.out.println("Closed folder and store...");
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}

	System.exit(0);
    }
}
