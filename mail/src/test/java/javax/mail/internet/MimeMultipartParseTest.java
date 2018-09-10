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

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
import javax.mail.util.*;
import javax.activation.*;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
 * Test multipart parsing.
 *
 * @author Bill Shannon
 */

public class MimeMultipartParseTest {
    private static Session session =
	Session.getInstance(new Properties(), null);

    private static final int maxsize = 10000;
    private static final String data =
	"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Test
    public void testParse() throws Exception {
	test(false);
    }

    @Test
    public void testParseShared() throws Exception {
	test(true);
    }

    /*
     * Test a few potential boundary cases, then test a range.
     * This is a compromise to make the run time of this test reasonable,
     * although it still takes about 30 seconds, which is on the long side
     * for a unit test.
     */
    public void test(boolean shared) throws Exception {
	testMessage(1, shared);
	testMessage(2, shared);
	testMessage(62, shared);
	testMessage(63, shared);
	testMessage(64, shared);
	testMessage(65, shared);
	testMessage(1023, shared);
	testMessage(1024, shared);
	testMessage(1025, shared);
	for (int size = 8100; size <= maxsize; size++)
	    testMessage(size, shared);
    }

    public void testMessage(int size, boolean shared) throws Exception {
	//System.out.println("SIZE: " + size);
	/*
	 * Construct a multipart message with a part of the
	 * given size.
	 */
	MimeMessage msg = new MimeMessage(session);
	msg.setFrom(new InternetAddress("me@example.com"));
	msg.setSubject("test multipart parsing");
	msg.setSentDate(new Date(0));
	MimeBodyPart mbp1 = new MimeBodyPart();
	mbp1.setText("main text\n");
	MimeBodyPart mbp3 = new MimeBodyPart();
	mbp3.setText("end text\n");
	MimeBodyPart mbp2 = new MimeBodyPart();
	byte[] part = new byte[size];
	for (int i = 0; i < size; i++) {
	    int j = i % 64;
	    if (j == 62)
		part[i] = (byte)'\r';
	    else if (j == 63)
		part[i] = (byte)'\n';
	    else
		part[i] = (byte)data.charAt((j + i / 64) % 62);
	}
	mbp2.setDataHandler(new DataHandler(
	    new ByteArrayDataSource(part, "text/plain")));

	MimeMultipart mp = new MimeMultipart();
	mp.addBodyPart(mbp1);
	mp.addBodyPart(mbp2);
	mp.addBodyPart(mbp3);
	msg.setContent(mp);
	msg.saveChanges();

	/*
	 * Write the message out to a byte array.
	 */
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	msg.writeTo(bos);
	bos.close();
	byte[] buf = bos.toByteArray();

	/*
	 * Construct a new message to parse the bytes.
	 */
	msg = new MimeMessage(session, shared ?
	    new SharedByteArrayInputStream(buf) :
	    new ByteArrayInputStream(buf));

	// verify that the part content is correct
	mp = (MimeMultipart)msg.getContent();
	mbp2 = (MimeBodyPart)mp.getBodyPart(1);
	InputStream is = mbp2.getInputStream();
	int k = 0;
	int c;
	while ((c = is.read()) >= 0) {
	    int j = k % 64;
	    byte e;
	    if (j == 62)
		e = (byte)'\r';
	    else if (j == 63)
		e = (byte)'\n';
	    else
		e = (byte)data.charAt((j + k / 64) % 62);
	    Assert.assertEquals("Size " + size + " at byte " + k, e, c);
	    k++;
	}
	Assert.assertEquals("Expected size", size, k);
    }
}
