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
import java.io.IOException;
import java.util.Properties;
import java.util.Date;

import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Flags;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test expunge of mbox folders.
 */
public final class MboxFolderExpungeTest {

    @BeforeClass
    public static void before() {
	System.setProperty("mail.mbox.locktype", "none");
    }

    @AfterClass
    public static void after() {
	System.getProperties().remove("mail.mbox.locktype");
    }

    @Test
    public void testRemoveFirst() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message m = f.getMessage(1);
	    m.setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    m = f.getMessage(1);
	    assertEquals("2", ((String)m.getContent()).trim());
	    m.setFlag(Flags.Flag.DELETED, true);
	    m = f.getMessage(2);
	    assertEquals("3", ((String)m.getContent()).trim());
	    f.expunge();
	    m = f.getMessage(1);
	    assertEquals("3", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveMiddle() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message m = f.getMessage(2);
	    m.setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    m = f.getMessage(1);
	    assertEquals("1", ((String)m.getContent()).trim());
	    m = f.getMessage(2);
	    assertEquals("3", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveLast() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message m = f.getMessage(3);
	    m.setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    m = f.getMessage(1);
	    assertEquals("1", ((String)m.getContent()).trim());
	    m = f.getMessage(2);
	    assertEquals("2", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveFirstClose() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message m = f.getMessage(1);
	    m.setFlag(Flags.Flag.DELETED, true);
	    f.close(true);
	    f.open(Folder.READ_WRITE);
	    m = f.getMessage(1);
	    assertEquals("2", ((String)m.getContent()).trim());
	    m.setFlag(Flags.Flag.DELETED, true);
	    m = f.getMessage(2);
	    assertEquals("3", ((String)m.getContent()).trim());
	    f.close(true);
	    f.open(Folder.READ_WRITE);
	    m = f.getMessage(1);
	    assertEquals("3", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveMiddleClose() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message m = f.getMessage(2);
	    m.setFlag(Flags.Flag.DELETED, true);
	    f.close(true);
	    f.open(Folder.READ_WRITE);
	    m = f.getMessage(1);
	    assertEquals("1", ((String)m.getContent()).trim());
	    m = f.getMessage(2);
	    assertEquals("3", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveLastClose() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message m = f.getMessage(3);
	    m.setFlag(Flags.Flag.DELETED, true);
	    f.close(true);
	    f.open(Folder.READ_WRITE);
	    m = f.getMessage(1);
	    assertEquals("1", ((String)m.getContent()).trim());
	    m = f.getMessage(2);
	    assertEquals("2", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveFirstMessages() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message[] msgs = f.getMessages(1, 3);
	    msgs[0].setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    assertEquals("2", ((String)msgs[1].getContent()).trim());
	    assertEquals("3", ((String)msgs[2].getContent()).trim());
	    msgs[1].setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    assertEquals("3", ((String)msgs[2].getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveMiddleMessages() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message[] msgs = f.getMessages(1, 3);
	    msgs[1].setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    assertEquals("1", ((String)msgs[0].getContent()).trim());
	    assertEquals("3", ((String)msgs[2].getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testRemoveLastMessages() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message[] msgs = f.getMessages(1, 3);
	    msgs[2].setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    assertEquals("1", ((String)msgs[0].getContent()).trim());
	    assertEquals("2", ((String)msgs[1].getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testNewMessagesAfterExpunge() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message[] msgs = f.getMessages(1, 3);
	    msgs[0].setFlag(Flags.Flag.DELETED, true);
	    f.expunge();
	    f.appendMessages(new Message[] { createMessage(null, 4) });
	    assertEquals(3, f.getMessageCount());
	    Message m = f.getMessage(3);
	    assertEquals("4", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    @Test
    public void testNewMessagesAfterClose() throws Exception {
	Folder f = createTestFolder();
	try {
	    f.open(Folder.READ_WRITE);
	    Message[] msgs = f.getMessages(1, 3);
	    msgs[0].setFlag(Flags.Flag.DELETED, true);
	    f.close(true);
	    f.appendMessages(new Message[] { createMessage(null, 4) });
	    f.open(Folder.READ_WRITE);
	    assertEquals(3, f.getMessageCount());
	    Message m = f.getMessage(3);
	    assertEquals("4", ((String)m.getContent()).trim());
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	} finally {
	    f.close(false);
	    f.delete(false);
	    f.getStore().close();
	}
    }

    /**
     * Create a temp file to use as a test folder and populate it
     * with 3 messages.
     */
    private Folder createTestFolder() {
	Properties properties = new Properties();
	Session session = Session.getInstance(properties);
	//session.setDebug(true);

	Folder folder = null;
	try {
	    Store store = session.getStore("mbox");
	    File temp = File.createTempFile("mbox", ".mbx");
	    temp.deleteOnExit();
	    store.connect();
	    folder = store.getFolder(temp.getAbsolutePath());
	    folder.create(Folder.HOLDS_MESSAGES);
	    Message[] msgs = new Message[3];
	    for (int i = 0; i < 3; i++)
		msgs[i] = createMessage(session, i + 1);
	    folder.appendMessages(msgs);
	} catch (Exception ex) {
	    System.out.println(ex);
	    //ex.printStackTrace();
	    fail(ex.toString());
	}
	return folder;
    }

    /**
     * Create a test message.
     */
    private Message createMessage(Session session, int msgno)
				throws MessagingException {
	MimeMessage msg = new MimeMessage(session);
	msg.setFrom("test@example.com");
	msg.setSentDate(new Date());
	String subject = "test ";
	// ensure each message is a different length
	for (int i = 0; i < msgno; i++)
	    subject += "test ";
	msg.setSubject(subject + msgno);
	msg.setText(msgno + "\n");
	msg.saveChanges();
	return msg;
    }
}
