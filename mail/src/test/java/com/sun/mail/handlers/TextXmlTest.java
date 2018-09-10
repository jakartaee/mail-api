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

package com.sun.mail.handlers;

import java.io.*;
import java.util.*;
import java.awt.datatransfer.DataFlavor;
import javax.activation.*;
import javax.mail.*;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.transform.stream.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the text/xml DataContentHandler.
 *
 * XXX - should test other Source objects in addition to StreamSource.
 *
 * @author Bill Shannon
 */

public class TextXmlTest {

    private static String xml = "<test><foo>bar</foo></test>\n";
    private static byte[] xmlBytes = xml.getBytes();

    // test InputStream to String
    @Test
    public void testStreamToStringTextXml() throws Exception {
	testStreamToString("text/xml");
    }

    // test InputStream to String
    @Test
    public void testStreamToStringApplicationXml() throws Exception {
	testStreamToString("application/xml");
    }

    private static void testStreamToString(String mimeType) throws Exception {
	DataContentHandler dch = new text_xml();
	DataFlavor df = new ActivationDataFlavor(String.class, mimeType, "XML");
	DataSource ds = new ByteArrayDataSource(xmlBytes, mimeType);
	Object content = dch.getContent(ds);
	assertEquals(String.class, content.getClass());
	assertEquals(xml, (String)content);
	content = dch.getTransferData(df, ds);
	assertEquals(String.class, content.getClass());
	assertEquals(xml, (String)content);
    }

    // test InputStream to StreamSource
    @Test
    public void testStreamToSource() throws Exception {
	DataContentHandler dch = new text_xml();
	DataFlavor df = new ActivationDataFlavor(StreamSource.class,
						    "text/xml", "XML stream");
	DataSource ds = new ByteArrayDataSource(xmlBytes, "text/xml");
	Object content = dch.getTransferData(df, ds);
	assertEquals(StreamSource.class, content.getClass());
	String sc = streamToString(((StreamSource)content).getInputStream());
	assertEquals(xml, sc);
    }

    // test String to OutputStream
    @Test
    public void testStringToStream() throws Exception {
	DataContentHandler dch = new text_xml();
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	dch.writeTo(xml, "text/xml", bos);
	String sc = new String(bos.toByteArray(), "us-ascii");
	assertEquals(xml, sc);
    }

    // test StreamSource to OutputStream
    @Test
    public void testSourceToStream() throws Exception {
	DataContentHandler dch = new text_xml();
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	StreamSource ss = new StreamSource(new ByteArrayInputStream(xmlBytes));
	dch.writeTo(ss, "text/xml", bos);
	String sc = new String(bos.toByteArray(), "us-ascii");
	// transformer adds an <?xml> header, so can't check for exact match
	assertTrue(sc.indexOf(xml.trim()) >= 0);
    }

    /**
     * Read a stream into a String.
     */
    private static String streamToString(InputStream is) {
	try {
	    StringBuilder sb = new StringBuilder();
	    int c;
	    while ((c = is.read()) > 0)
		sb.append((char)c);
	    return sb.toString();
	} catch (IOException ex) {
	    return "";
	} finally {
	    try {
		is.close();
	    } catch (IOException cex) { }
	}
    }
}
