/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.dsn;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetHeaders;

import org.junit.*;
import static org.junit.Assert.assertTrue;

/**
 */
public class MultipartReportTest {
 
    private static Session session = Session.getInstance(new Properties());

    @Test
    public void testWrongIndexBug() throws Exception {
	MimeMessage msg = new MimeMessage(session);

	// create the Multipart and its parts to it
	MultipartReport mp = new MultipartReport();
	mp.setText("test Multipart Report\n");

	DeliveryStatus ds = new DeliveryStatus();
	InternetHeaders mdsn = new InternetHeaders();
	mdsn.setHeader("Reporting-MTA", "test");
	ds.setMessageDSN(mdsn);
	InternetHeaders rdsn = new InternetHeaders();
	rdsn.setHeader("Final-Recipient", "joe");
	rdsn.setHeader("Action", "none");
	rdsn.setHeader("Status", "none");
	ds.addRecipientDSN(rdsn);
	mp.setReport(ds);
	msg.setContent(mp);
	msg.saveChanges();
	msg.writeTo(new NullOutputStream());
	// anything other than an exception is success
	assertTrue("MultipartReport constructed", true);
    }
}
