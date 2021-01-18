/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.io.*;
import jakarta.activation.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 * A special MimeBodyPart used with MSMessage.
 *
 * @author John Mani
 * @author Bill Shannon
 */
public class MSBodyPart extends MimeBodyPart {
    private int start;
    private int end;
    private String type = UNKNOWN;
    private String disposition;
    private String encoding;
    private String filename = UNKNOWN;

    private static final String UNKNOWN = "UNKNOWN";

    public MSBodyPart(byte[] content, int start, int end,
		String disposition, String encoding) {
	this.content = content;
	this.start = start;
	this.end = end;
	this.disposition = disposition;
	this.encoding = encoding;
    }

    public String getContentType() throws MessagingException {
	// try to figure this out from the filename extension
	if (type == UNKNOWN)
	    processBegin();
	return type;
    }

    public String getEncoding() throws MessagingException {
	return encoding;
    }

    public String getDisposition() throws MessagingException {
	return disposition;
    }

    public String getFileName() throws MessagingException {
	// get filename from the "begin" line
	if (filename == UNKNOWN)
	    processBegin();
	return filename;
    }

    protected InputStream getContentStream() {
	return new ByteArrayInputStream(content, start, end - start);
    }

    /**
     * Process the "begin" line to extract the filename,
     * and from it determine the Content-Type.
     */
    private void processBegin() {
	InputStream in = getContentStream();
	try {
	    BufferedReader r = new BufferedReader(new InputStreamReader(in));
	    String begin = r.readLine();
	    // format is "begin 666 filename.txt"
	    if (begin != null && begin.regionMatches(true, 0, "begin ", 0, 6)) {
		int i = begin.indexOf(' ', 6);
		if (i > 0) {
		    filename = begin.substring(i + 1);
		    FileTypeMap map = FileTypeMap.getDefaultFileTypeMap();
		    type = map.getContentType(filename);
		    if (type == null)
			type = "application/octet-stream";
		}
	    }
	} catch (IOException ex) {
	    // ignore
	} finally {
	    try {
		in.close();
	    } catch (IOException ex) {
		// ignore it
	    }
	    if (filename == UNKNOWN)
		filename = null;
	    if (type == UNKNOWN || type == null)
		type = "text/plain";
	}
    }
}
