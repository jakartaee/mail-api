/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.smtp;

import java.util.Properties;
import java.net.ServerSocket;

import javax.mail.Session;
import javax.mail.Transport;

import com.sun.mail.util.MailConnectException;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test connect failures.
 */
public class SMTPConnectFailureTest {

    private static final String HOST = "localhost";
    private static final int CTO = 20;

    @Test
    public void testNoServer() {
        try {
	    // verify that port is not being used
	    ServerSocket ss = new ServerSocket(0);
	    int port = ss.getLocalPort();
	    ss.close();
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", HOST);
            properties.setProperty("mail.smtp.port", "" + port);
            properties.setProperty("mail.smtp.connectiontimeout", "" + CTO);
            Session session = Session.getInstance(properties);
            //session.setDebug(true);

            Transport t = session.getTransport("smtp");
            try {
                t.connect("test", "test");
		fail("Connected!");
		// failure!
	    } catch (MailConnectException mcex) {
		// success!
		assertEquals(HOST, mcex.getHost());
		assertEquals(port, mcex.getPort());
		assertEquals(CTO, mcex.getConnectionTimeout());
	    } catch (Exception ex) {
		// expect an exception when connect times out
		fail(ex.toString());
            } finally {
		if (t.isConnected())
		    t.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
