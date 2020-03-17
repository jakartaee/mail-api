/*
 * Copyright (c) 2001, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package demo;

import java.util.*;
import jakarta.mail.*;
import javax.naming.*;

/**
 * This JavaBean is used to store mail user information.
 */
public class MailUserBean {
    private Folder folder;
    private String hostname;
    private String username;
    private String password;
    private Session session;
    private Store store;
    private URLName url;
    private String protocol = "imap";
    private String mbox = "INBOX";	

    public MailUserBean(){}

    /**
     * Returns the jakarta.mail.Folder object.
     */
    public Folder getFolder() {
	return folder;
    }
    
    /**
     * Returns the number of messages in the folder.
     */
    public int getMessageCount() throws MessagingException {
	return folder.getMessageCount();
    }

    /**
     * hostname getter method.
     */
    public String getHostname() {
	return hostname;
    }
    
    /**
     * hostname setter method.
     */
    public void setHostname(String hostname) {
	this.hostname = hostname;
    }
	
    /**
     * username getter method.
     */
    public String getUsername() {
	return username;
    }

    /**
     * username setter method.
     */
    public void setUsername(String username) {
	this.username = username;
    }

    /**
     * password getter method.
     */
    public String getPassword() {
	return password;
    }

    /**
     * password setter method.
     */
    public void setPassword(String password) {
	this.password = password;
    }

    /**
     * session getter method.
     */
    public Session getSession() {
	return session;
    }

    /**
     * session setter method.
     */
    public void setSession(Session session) {
	this.session = session;
    }

    /**
     * store getter method.
     */
    public Store getStore() {
	return store;
    }

    /**
     * store setter method.
     */
    public void setStore(Store store) {
	this.store = store;
    }

    /**
     * url getter method.
     */
    public URLName getUrl() {
	return url;
    }

    /**
     * Method for checking if the user is logged in.
     */
    public boolean isLoggedIn() {
	return store.isConnected();
    }
      
    /**
     * Method used to login to the mail host.
     */
    public void login() throws Exception {
	url = new URLName(protocol, getHostname(), -1, mbox, 
			  getUsername(), getPassword());
	/*
	 * First, try to get the session from JNDI,
	 * as would be done under J2EE.
	 */
	try {
	    InitialContext ic = new InitialContext();
	    Context ctx = (Context)ic.lookup("java:comp/env");
	    session = (Session)ctx.lookup("MySession");
	} catch (Exception ex) {
	    // ignore it
	}

	// if JNDI fails, try the old way that should work everywhere
	if (session == null) {
	    Properties props = null;
	    try {
		props = System.getProperties();
	    } catch (SecurityException sex) {
		props = new Properties();
	    }
	    session = Session.getInstance(props, null);
	}
	store = session.getStore(url);
	store.connect();
	folder = store.getFolder(url);
	
	folder.open(Folder.READ_WRITE);
    }

    /**
     * Method used to login to the mail host.
     */
    public void login(String hostname, String username, String password) 
	throws Exception {
	    
	this.hostname = hostname;
	this.username = username;
	this.password = password;
	    
	login();
    }

    /**
     * Method used to logout from the mail host.
     */
    public void logout() throws MessagingException {
	folder.close(false);
	store.close();
	store = null;
	session = null;
    }
}

