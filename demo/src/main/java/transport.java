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
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.event.*;
import jakarta.activation.*;

/**
 * transport is a simple program that creates a message, explicitly
 * retrieves a Transport from the session based on the type of the
 * address (it's InternetAddress, so SMTP will be used) and sends 
 * the message.
 *
 * usage: <code>java transport <i>"toaddr1[, toaddr2]*"  from smtphost 
 * true|false</i></code><br>
 * where <i>to</i> and <i>from</i> are the destination and
 * origin email addresses, respectively, and <i>smtphost</i>
 * is the hostname of the machine that has the smtp server
 * running. The <i>to</i> addresses can be either a single email
 * address or a comma-separated list of email addresses in
 * quotes, i.e. "joe@machine, jane, max@server.com"
 * The last parameter either turns on or turns off
 * debugging during sending.
 *
 * @author Max Spivak
 */
public class transport implements ConnectionListener, TransportListener {
    static String msgText = "This is a message body.\nHere's the second line.";
    static String msgText2 = "\nThis was sent by transport.java demo program.";

    public static void main(String[] args) {
	Properties props = System.getProperties();
	// parse the arguments
	InternetAddress[] addrs = null;
	InternetAddress from;
	boolean debug = false;
	if (args.length != 4) {
	    usage();
	    return;
	} else {
	    props.put("mail.smtp.host", args[2]);
	    if (args[3].equals("true")) {
		debug = true;
	    } else if (args[3].equals("false")) {
		debug = false;
	    } else {
		usage();
		return;
	    }

	    // parse the destination addresses
	    try {
		addrs = InternetAddress.parse(args[0], false);
		from = new InternetAddress(args[1]);
	    } catch (AddressException aex) {
		System.out.println("Invalid Address");
		aex.printStackTrace();
		return;
	    }
	}
	// create some properties and get a Session
	Session session = Session.getInstance(props, null);
	session.setDebug(debug);

	transport t = new transport();
	t.go(session, addrs, from);
    }

    public transport() {}

    public void go(Session session, InternetAddress[] toAddr,
				InternetAddress from) {
	Transport trans = null;

	try {
	    // create a message
	    Message msg = new MimeMessage(session);
	    msg.setFrom(from);
	    msg.setRecipients(Message.RecipientType.TO, toAddr);
	    msg.setSubject("Jakarta Mail APIs transport.java Test");
	    msg.setSentDate(new Date());  // Date: header
	    msg.setContent(msgText+msgText2, "text/plain");
	    msg.saveChanges();

	    // get the smtp transport for the address
	    trans = session.getTransport(toAddr[0]);

	    // register ourselves as listener for ConnectionEvents 
	    // and TransportEvents
	    trans.addConnectionListener(this);
	    trans.addTransportListener(this);

	    // connect the transport
	    trans.connect();

	    // send the message
	    trans.sendMessage(msg, toAddr);

	    // give the EventQueue enough time to fire its events
	    try {Thread.sleep(5);}catch(InterruptedException e) {}

	} catch (MessagingException mex) {
	    // give the EventQueue enough time to fire its events
	    try {Thread.sleep(5);}catch(InterruptedException e) {}

	    System.out.println("Sending failed with exception:");
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
	} finally {
	    try {
		// close the transport
		if (trans != null)
		    trans.close();
	    } catch (MessagingException mex) { /* ignore */ }
	}
    }

    // implement ConnectionListener interface
    public void opened(ConnectionEvent e) {
	System.out.println(">>> ConnectionListener.opened()");
    }
    public void disconnected(ConnectionEvent e) {}
    public void closed(ConnectionEvent e) {
	System.out.println(">>> ConnectionListener.closed()");
    }

    // implement TransportListener interface
    public void messageDelivered(TransportEvent e) {
	System.out.println(">>> TransportListener.messageDelivered().");
	System.out.println(" Valid Addresses:");
	Address[] valid = e.getValidSentAddresses();
	if (valid != null) {
	    for (int i = 0; i < valid.length; i++) 
		System.out.println("    " + valid[i]);
	}
    }
    public void messageNotDelivered(TransportEvent e) {
	System.out.println(">>> TransportListener.messageNotDelivered().");
	System.out.println(" Invalid Addresses:");
	Address[] invalid = e.getInvalidAddresses();
	if (invalid != null) {
	    for (int i = 0; i < invalid.length; i++) 
		System.out.println("    " + invalid[i]);
	}
    }
    public void messagePartiallyDelivered(TransportEvent e) {
	System.out.println(">>> TransportListener.messagePartiallyDelivered().");
	System.out.println(" Valid Addresses:");
	Address[] valid = e.getValidSentAddresses();
	if (valid != null) {
	    for (int i = 0; i < valid.length; i++) 
		System.out.println("    " + valid[i]);
	}
	System.out.println(" Valid Unsent Addresses:");
	Address[] unsent = e.getValidUnsentAddresses();
	if (unsent != null) {
	    for (int i = 0; i < unsent.length; i++) 
		System.out.println("    " + unsent[i]);
	}
	System.out.println(" Invalid Addresses:");
	Address[] invalid = e.getInvalidAddresses();
	if (invalid != null) {
	    for (int i = 0; i < invalid.length; i++) 
		System.out.println("    " + invalid[i]);
	}
    }

    private static void usage() {
	System.out.println(
    "usage: java transport \"<to1>[, <to2>]*\" <from> <smtp> true|false");
	System.out.println(
    "example: java transport \"joe@machine, jane\" senderaddr smtphost false");
    }
}
