/*
 * Copyright (c) 2026 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail.internet;

import jakarta.mail.Header;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test the order in which InternetHeaders adds trace headers.
 */
public class InternetHeadersTest {

    /**
     * Delivered-To headers are placed at the top of the headers,
     * newest first, like Received headers (RFC 9228, section 4).
     */
    @Test
    public void testDeliveredToAddedAtTop() throws Exception {
        InternetHeaders h = new InternetHeaders();
        h.addHeader("Subject", "test");
        h.addHeader("Delivered-To", "first@example.com");
        h.addHeader("Delivered-To", "second@example.com");
        assertEquals(List.of(
                "Delivered-To: second@example.com",
                "Delivered-To: first@example.com",
                "Subject: test"), lines(h));
    }

    /**
     * Received and Delivered-To headers are never reordered with
     * respect to each other; each new one goes on top of both
     * (the example in RFC 9228, section 5).
     */
    @Test
    public void testTraceHeadersKeepHandlingSequence() throws Exception {
        InternetHeaders h = new InternetHeaders();
        h.addHeader("From", "author@com.example");
        h.addHeader("Received", "by mail.com.example");
        h.addHeader("Received", "by submit.org.example");
        h.addHeader("Delivered-To", "list@org.example");
        h.addHeader("Received", "from mail.org.example by relay.edu.example");
        h.addHeader("Delivered-To", "Recipient-alumn@edu.example");
        assertEquals(List.of(
                "Delivered-To: Recipient-alumn@edu.example",
                "Received: from mail.org.example by relay.edu.example",
                "Delivered-To: list@org.example",
                "Received: by submit.org.example",
                "Received: by mail.com.example",
                "From: author@com.example"), lines(h));
    }

    /**
     * A Return-Path header stays above the trace headers.
     */
    @Test
    public void testReturnPathStaysFirst() throws Exception {
        InternetHeaders h = new InternetHeaders();
        h.addHeader("Received", "by mail.com.example");
        h.addHeader("Return-Path", "<author@com.example>");
        h.addHeader("Delivered-To", "list@org.example");
        assertEquals(List.of(
                "Return-Path: <author@com.example>",
                "Delivered-To: list@org.example",
                "Received: by mail.com.example"), lines(h));
    }

    /**
     * Headers loaded from a message keep their order, and a new
     * Delivered-To header goes above the existing trace headers.
     */
    @Test
    public void testDeliveredToOnLoadedHeaders() throws Exception {
        String msg =
                "Delivered-To: list@org.example\r\n" +
                "Received: by submit.org.example\r\n" +
                "From: author@com.example\r\n" +
                "\r\n";
        InternetHeaders h = new InternetHeaders(
                new ByteArrayInputStream(msg.getBytes(StandardCharsets.US_ASCII)));
        h.addHeader("Received", "from mail.org.example by relay.edu.example");
        h.addHeader("Delivered-To", "Recipient-alumn@edu.example");
        assertEquals(List.of(
                "Delivered-To: Recipient-alumn@edu.example",
                "Received: from mail.org.example by relay.edu.example",
                "Delivered-To: list@org.example",
                "Received: by submit.org.example",
                "From: author@com.example"), lines(h));
    }

    private static List<String> lines(InternetHeaders h) {
        List<String> result = new ArrayList<>();
        Enumeration<Header> e = h.getAllHeaders();
        while (e.hasMoreElements()) {
            Header hdr = e.nextElement();
            result.add(hdr.getName() + ": " + hdr.getValue());
        }
        return result;
    }
}
