/*
 * Copyright (c) 1996, 2020 Oracle and/or its affiliates. All rights reserved.
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
import jakarta.mail.event.*;
import jakarta.activation.*;

import com.sun.mail.imap.*;

/* Monitors given mailbox for new mail */

public class monitor {

    public static void main(String argv[]) {
	if (argv.length != 5) {
	    System.out.println(
		"Usage: monitor <host> <user> <password> <mbox> <freq>");
	    System.exit(1);
	}
	System.out.println("\nTesting monitor\n");

	try {
	    Properties props = System.getProperties();

	    // Get a Session object
	    Session session = Session.getInstance(props, null);
	    // session.setDebug(true);

	    // Get a Store object
	    Store store = session.getStore("imap");

	    // Connect
	    store.connect(argv[0], argv[1], argv[2]);

	    // Open a Folder
	    Folder folder = store.getFolder(argv[3]);
	    if (folder == null || !folder.exists()) {
		System.out.println("Invalid folder");
		System.exit(1);
	    }

	    folder.open(Folder.READ_WRITE);

	    // Add messageCountListener to listen for new messages
	    folder.addMessageCountListener(new MessageCountAdapter() {
		public void messagesAdded(MessageCountEvent ev) {
		    Message[] msgs = ev.getMessages();
		    System.out.println("Got " + msgs.length + " new messages");

		    // Just dump out the new messages
		    for (int i = 0; i < msgs.length; i++) {
			try {
			    System.out.println("-----");
			    System.out.println("Message " +
				msgs[i].getMessageNumber() + ":");
			    msgs[i].writeTo(System.out);
			} catch (IOException ioex) { 
			    ioex.printStackTrace();	
			} catch (MessagingException mex) {
			    mex.printStackTrace();
			}
		    }
		}
	    });
			
	    // Check mail once in "freq" MILLIseconds
	    int freq = Integer.parseInt(argv[4]);
	    boolean supportsIdle = false;
	    try {
		if (folder instanceof IMAPFolder) {
		    IMAPFolder f = (IMAPFolder)folder;
		    f.idle();
		    supportsIdle = true;
		}
	    } catch (FolderClosedException fex) {
		throw fex;
	    } catch (MessagingException mex) {
		supportsIdle = false;
	    }
	    for (;;) {
		if (supportsIdle && folder instanceof IMAPFolder) {
		    IMAPFolder f = (IMAPFolder)folder;
		    f.idle();
		    System.out.println("IDLE done");
		} else {
		    Thread.sleep(freq); // sleep for freq milliseconds

		    // This is to force the IMAP server to send us
		    // EXISTS notifications. 
		    folder.getMessageCount();
		}
	    }

	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
