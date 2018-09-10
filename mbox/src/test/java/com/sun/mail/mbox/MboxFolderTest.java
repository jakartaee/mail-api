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

package com.sun.mail.mbox;

import java.io.File;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test of mbox folders.
 */
public final class MboxFolderTest {

    @BeforeClass
    public static void before() {
	System.setProperty("mail.mbox.locktype", "none");
    }

    @AfterClass
    public static void after() {
	System.getProperties().remove("mail.mbox.locktype");
    }

    /**
     * Test that a mailbox that has garbage at the beginning
     * (such as a gratuitous blank line) is handled without
     * crashing and without corrupting the mailbox.
     */
    @Test
    public void testGarbageAtStartOfFolder() throws Exception {
	Folder f = null;
	try {
	    File temp = File.createTempFile("mbox", ".mbx");
	    temp.deleteOnExit();
	    PrintWriter pw = new PrintWriter(temp);
	    pw.println();
	    pw.println("From - Tue Aug 23 11:56:51 2011");
	    pw.println();
	    pw.println("test");
	    pw.println();
	    pw.close();
	    long size = temp.length();

	    Properties properties = new Properties();
	    Session session = Session.getInstance(properties);
	    Store store = session.getStore("mbox");
	    store.connect();
	    f = store.getFolder(temp.getAbsolutePath());
	    f.open(Folder.READ_WRITE);
	    assertEquals(0, f.getMessageCount());
	    f.close(true);
	    assertEquals(size, temp.length());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    if (f != null) {
		f.delete(false);
		f.getStore().close();
	    }
	}
    }
}
