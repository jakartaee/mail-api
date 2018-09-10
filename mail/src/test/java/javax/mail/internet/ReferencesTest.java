/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 * Test setting of the References header.
 *
 * @author Bill Shannon
 */
public class ReferencesTest {
    private static Session session = Session.getInstance(new Properties());

    /*
     * Test cases:
     * 
     * Message-Id	References	In-Reply-To	Expected Result
     */

    @Test
    public void test1() throws MessagingException {
	test(null,	null,		null,		null);
    }

    @Test
    public void test2() throws MessagingException {
	test(null,	null,		"<1@a>",	"<1@a>");
    }

    @Test
    public void test3() throws MessagingException {
	test(null,	"<2@b>",	null,		"<2@b>");
    }

    @Test
    public void test4() throws MessagingException {
	test(null,	"<2@b>",	"<1@a>",	"<2@b>");
    }

    @Test
    public void test5() throws MessagingException {
	test("<3@c>",	null,		null,		"<3@c>");
    }

    @Test
    public void test6() throws MessagingException {
	test("<3@c>",	null,		"<1@a>",	"<1@a> <3@c>");
    }

    @Test
    public void test7() throws MessagingException {
	test("<3@c>",	"<2@b>",	null,		"<2@b> <3@c>");
    }

    @Test
    public void test8() throws MessagingException {
	test("<3@c>",	"<2@b>",	"<1@a>",	"<2@b> <3@c>");
    }

    private static void test(String msgid, String ref, String irt, String res)
				throws MessagingException {
	MimeMessage msg = new MimeMessage(session);
	msg.setFrom();
	msg.setRecipients(Message.RecipientType.TO, "you@example.com");
	msg.setSubject("test");
	if (msgid != null)
	    msg.setHeader("Message-Id", msgid);
	if (ref != null)
	    msg.setHeader("References", ref);
	if (irt != null)
	    msg.setHeader("In-Reply-To", irt);
	msg.setText("text");

	MimeMessage reply = (MimeMessage)msg.reply(false);
	String rref = reply.getHeader("References", " ");

	assertEquals(res, rref);
    }
}
