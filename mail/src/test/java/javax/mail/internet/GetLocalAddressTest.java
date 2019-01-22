/*
 * Copyright (c) 2010, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package javax.mail.internet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 * Test the InternetAddress.getLocalAddress() method.
 */
public class GetLocalAddressTest {
 
    private static String localhost;
    static {
	try {
	    localhost = InetAddress.getLocalHost().getCanonicalHostName();
	    // if the host name and host address are the same, the name
	    // is really an address and we need to convert it to an
	    // internet address literal to use it in an email address
	    if (localhost.equals(InetAddress.getLocalHost().getHostAddress()))
		localhost = "[" + localhost + "]";
	} catch (UnknownHostException ex) {
	    localhost = "localhost";
	}
    }

    @Test
    public void testUserName() throws Exception {
	System.setProperty("user.name", "Joe");
	InternetAddress ia = InternetAddress.getLocalAddress(null);
	assertEquals("Joe@" + localhost, ia.getAddress());
    }

    @Test
    public void testUserNameSession() throws Exception {
	System.setProperty("user.name", "Joe");
	Session s = Session.getInstance(new Properties());
	InternetAddress ia = InternetAddress.getLocalAddress(s);
	assertEquals("Joe@" + localhost, ia.getAddress());
    }

    @Test
    public void testMailFrom() throws Exception {
	System.setProperty("user.name", "Joe");
	Properties p = new Properties();
	p.setProperty("mail.from", "Bob@home");
	Session s = Session.getInstance(p);
	InternetAddress ia = InternetAddress.getLocalAddress(s);
	assertEquals("Bob@home", ia.getAddress());
    }

    @Test
    public void testMailFromAddress() throws Exception {
	System.setProperty("user.name", "Joe");
	Properties p = new Properties();
	p.setProperty("mail.from", "Bob <Bob@home>");
	Session s = Session.getInstance(p);
	InternetAddress ia = InternetAddress.getLocalAddress(s);
	assertEquals("Bob@home", ia.getAddress());
    }

    @Test
    public void testMailUser() throws Exception {
	System.setProperty("user.name", "Joe");
	Properties p = new Properties();
	p.setProperty("mail.user", "Bob");
	Session s = Session.getInstance(p);
	InternetAddress ia = InternetAddress.getLocalAddress(s);
	assertEquals("Bob@" + localhost, ia.getAddress());
    }

    @Test
    public void testMailUserSpace() throws Exception {
	System.setProperty("user.name", "Joe");
	Properties p = new Properties();
	p.setProperty("mail.user", "NETWORK SERVICE");
	Session s = Session.getInstance(p);
	InternetAddress ia = InternetAddress.getLocalAddress(s);
	assertEquals("\"NETWORK SERVICE\"@" + localhost, ia.getAddress());
    }

    @Test
    public void testMailHost() throws Exception {
	System.setProperty("user.name", "Joe");
	Properties p = new Properties();
	p.setProperty("mail.user", "Bob");
	p.setProperty("mail.host", "home");
	Session s = Session.getInstance(p);
	InternetAddress ia = InternetAddress.getLocalAddress(s);
	assertEquals("Bob@home", ia.getAddress());
    }
}
