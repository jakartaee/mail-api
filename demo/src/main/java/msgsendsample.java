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
 * msgsendsample creates a very simple text/plain message and sends it.
 * <p>
 * usage: <code>java msgsendsample <i>to from smtphost true|false</i></code>
 * where <i>to</i> and <i>from</i> are the destination and
 * origin email addresses, respectively, and <i>smtphost</i>
 * is the hostname of the machine that has the smtp server
 * running. The last parameter either turns on or turns off
 * debugging during sending.
 *
 * @author Max Spivak
 */
public class msgsendsample {
    static String msgText = "This is a message body.\nHere's the second line.";

    public static void main(String[] args) {
	if (args.length != 4) {
	    usage();
	    System.exit(1);
	}

	System.out.println();
	
	String to = args[0];
	String from = args[1];
	String host = args[2];
	boolean debug = Boolean.valueOf(args[3]).booleanValue();

	// create some properties and get the default Session
	Properties props = new Properties();
	props.put("mail.smtp.host", host);
	if (debug) props.put("mail.debug", args[3]);

	Session session = Session.getInstance(props, null);
	session.setDebug(debug);
	
	try {
	    // create a message
	    MimeMessage msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));
	    InternetAddress[] address = {new InternetAddress(to)};
	    msg.setRecipients(Message.RecipientType.TO, address);
	    msg.setSubject("Jakarta Mail APIs Test");
	    msg.setSentDate(new Date());
	    // If the desired charset is known, you can use
	    // setText(text, charset)
	    msg.setText(msgText);
	    
	    Transport.send(msg);
	} catch (MessagingException mex) {
	    System.out.println("\n--Exception handling in msgsendsample.java");

	    mex.printStackTrace();
	    System.out.println();
	    Exception ex = mex;
	    do {
		if (ex instanceof SendFailedException) {
		    SendFailedException sfex = (SendFailedException)ex;
		    Address[] invalid = sfex.getInvalidAddresses();
		    if (invalid != null) {
			System.out.println("    ** Invalid Addresses");
			for (int i = 0; i < invalid.length; i++) 
			    System.out.println("         " + invalid[i]);
		    }
		    Address[] validUnsent = sfex.getValidUnsentAddresses();
		    if (validUnsent != null) {
			System.out.println("    ** ValidUnsent Addresses");
			for (int i = 0; i < validUnsent.length; i++) 
			    System.out.println("         "+validUnsent[i]);
		    }
		    Address[] validSent = sfex.getValidSentAddresses();
		    if (validSent != null) {
			System.out.println("    ** ValidSent Addresses");
			for (int i = 0; i < validSent.length; i++) 
			    System.out.println("         "+validSent[i]);
		    }
		}
		System.out.println();
		if (ex instanceof MessagingException)
		    ex = ((MessagingException)ex).getNextException();
		else
		    ex = null;
	    } while (ex != null);
	}
    }

    private static void usage() {
	System.out.println("usage: java msgsendsample <to> <from> <smtp> true|false");
    }
}
