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
import jakarta.mail.internet.*;
import jakarta.activation.*;

/**
 * msgmultisendsample creates a simple multipart/mixed message and sends it.
 * Both body parts are text/plain.
 * <p>
 * usage: <code>java msgmultisendsample <i>to from smtp true|false</i></code>
 * where <i>to</i> and <i>from</i> are the destination and
 * origin email addresses, respectively, and <i>smtp</i>
 * is the hostname of the machine that has smtp server
 * running.  The last parameter either turns on or turns off
 * debugging during sending.
 *
 * @author	Max Spivak
 */
public class msgmultisendsample {
    static String msgText1 = "This is a message body.\nHere's line two.";
    static String msgText2 = "This is the text in the message attachment.";

    public static void main(String[] args) {
	if (args.length != 4) {
	    System.out.println("usage: java msgmultisend <to> <from> <smtp> true|false");
	    return;
	}

	String to = args[0];
	String from = args[1];
	String host = args[2];
	boolean debug = Boolean.valueOf(args[3]).booleanValue();

	// create some properties and get the default Session
	Properties props = new Properties();
	props.put("mail.smtp.host", host);

	Session session = Session.getInstance(props, null);
	session.setDebug(debug);
	
	try {
	    // create a message
	    MimeMessage msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));
	    InternetAddress[] address = {new InternetAddress(to)};
	    msg.setRecipients(Message.RecipientType.TO, address);
	    msg.setSubject("Jakarta Mail APIs Multipart Test");
	    msg.setSentDate(new Date());

	    // create and fill the first message part
	    MimeBodyPart mbp1 = new MimeBodyPart();
	    mbp1.setText(msgText1);

	    // create and fill the second message part
	    MimeBodyPart mbp2 = new MimeBodyPart();
	    // Use setText(text, charset), to show it off !
	    mbp2.setText(msgText2, "us-ascii");

	    // create the Multipart and its parts to it
	    Multipart mp = new MimeMultipart();
	    mp.addBodyPart(mbp1);
	    mp.addBodyPart(mbp2);

	    // add the Multipart to the message
	    msg.setContent(mp);
	    
	    // send the message
	    Transport.send(msg);
	} catch (MessagingException mex) {
	    mex.printStackTrace();
	    Exception ex = null;
	    if ((ex = mex.getNextException()) != null) {
		ex.printStackTrace();
	    }
	}
    }
}
