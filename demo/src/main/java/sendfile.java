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
 * sendfile will create a multipart message with the second
 * block of the message being the given file.<p>
 *
 * This demonstrates how to use the FileDataSource to send
 * a file via mail.<p>
 *
 * usage: <code>java sendfile <i>to from smtp file true|false</i></code>
 * where <i>to</i> and <i>from</i> are the destination and
 * origin email addresses, respectively, and <i>smtp</i>
 * is the hostname of the machine that has smtp server
 * running.  <i>file</i> is the file to send. The next parameter
 * either turns on or turns off debugging during sending.
 *
 * @author	Christopher Cotton
 */
public class sendfile {

    public static void main(String[] args) {
	if (args.length != 5) {
	    System.out.println("usage: java sendfile <to> <from> <smtp> <file> true|false");
	    System.exit(1);
	}

	String to = args[0];
	String from = args[1];
	String host = args[2];
	String filename = args[3];
	boolean debug = Boolean.valueOf(args[4]).booleanValue();
	String msgText1 = "Sending a file.\n";
	String subject = "Sending a file";
	
	// create some properties and get the default Session
	Properties props = System.getProperties();
	props.put("mail.smtp.host", host);
	
	Session session = Session.getInstance(props, null);
	session.setDebug(debug);
	
	try {
	    // create a message
	    MimeMessage msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress(from));
	    InternetAddress[] address = {new InternetAddress(to)};
	    msg.setRecipients(Message.RecipientType.TO, address);
	    msg.setSubject(subject);

	    // create and fill the first message part
	    MimeBodyPart mbp1 = new MimeBodyPart();
	    mbp1.setText(msgText1);

	    // create the second message part
	    MimeBodyPart mbp2 = new MimeBodyPart();

	    // attach the file to the message
	    mbp2.attachFile(filename);

	    /*
	     * Use the following approach instead of the above line if
	     * you want to control the MIME type of the attached file.
	     * Normally you should never need to do this.
	     *
	    FileDataSource fds = new FileDataSource(filename) {
		public String getContentType() {
		    return "application/octet-stream";
		}
	    };
	    mbp2.setDataHandler(new DataHandler(fds));
	    mbp2.setFileName(fds.getName());
	     */

	    // create the Multipart and add its parts to it
	    Multipart mp = new MimeMultipart();
	    mp.addBodyPart(mbp1);
	    mp.addBodyPart(mbp2);

	    // add the Multipart to the message
	    msg.setContent(mp);

	    // set the Date: header
	    msg.setSentDate(new Date());

	    /*
	     * If you want to control the Content-Transfer-Encoding
	     * of the attached file, do the following.  Normally you
	     * should never need to do this.
	     *
	    msg.saveChanges();
	    mbp2.setHeader("Content-Transfer-Encoding", "base64");
	     */

	    // send the message
	    Transport.send(msg);
	    
	} catch (MessagingException mex) {
	    mex.printStackTrace();
	    Exception ex = null;
	    if ((ex = mex.getNextException()) != null) {
		ex.printStackTrace();
	    }
	} catch (IOException ioex) {
	    ioex.printStackTrace();
	}
    }
}
