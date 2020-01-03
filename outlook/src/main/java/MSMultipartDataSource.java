/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

/**
 * A special MultipartDataSource used with MSMessage.
 *
 * @author John Mani
 * @author Bill Shannon
 */
public class MSMultipartDataSource extends MimePartDataSource
				implements MultipartDataSource {
    //private List<MSBodyPart> parts;
    private List parts;

    public MSMultipartDataSource(MimePart part, byte[] content)
				throws MessagingException {
	super(part);
	//parts = new ArrayList<MSBodyPart>();
	parts = new ArrayList();

	/*
	 * Parse the text of the message to find the attachments.
	 *
	 * Currently we just look for the lines that mark the
	 * begin and end of uuencoded data, but this can be
	 * fooled by similar text in the message body.  Instead,
	 * we could use the Encoding header, which indicates how
	 * many lines are in each body part.  For example:
	 *
	 * Encoding: 41 TEXT, 38 UUENCODE, 3155 UUENCODE, 1096 UUENCODE
	 *
	 * Similarly, we could get the filenames of the attachments
	 * from the X-MS-Attachment headers.  For example:
	 *
	 * X-MS-Attachment: ATT00000.htx 0 00-00-1980 00:00
	 * X-MS-Attachment: Serengeti 2GG.mpp 0 00-00-1980 00:00
	 * X-MS-Attachment: project team update 031298.doc 0 00-00-1980 00:00
	 *
	 * (Note that there might be unquoted spaces in the filename.)
	 */
	int pos = startsWith(content, 0, "begin");
	if (pos == -1)
	    throw new MessagingException("invalid multipart");
	
	if (pos > 0)	// we have an unencoded main body part
	    parts.add(new MSBodyPart(content, 0, pos, "inline", "7bit"));
	else		// no main body part
	    pos = 0;

	// now collect all the uuencoded individual body parts
	int start;
	for (;;) {
	    start = startsWith(content, pos, "begin");
	    if (start == -1)
		break;
	    pos = startsWith(content, start, "end");
	    if (pos == -1)
		break;
	    pos += 3;	// skip to the end of "end"
	    parts.add(new MSBodyPart(content, start, pos,
					"attachment", "uuencode"));
	}
    }

    public int getCount() {
	return parts.size();
    }

    public BodyPart getBodyPart(int index) throws MessagingException {
	return (BodyPart)parts.get(index);
    }

    /**
     * This method scans the given byte[], beginning at "start", for
     * lines that begin with the sequence "seq".  If found, the start
     * position of the sequence within the byte[] is returned.
     */
    private int startsWith(byte[] content, int start, String seq) {
	int slen = seq.length();
	boolean bol = true;
	for (int i = start; i < content.length; i++) {
	    if (bol) {
		if ((i + slen) < content.length) {
		    String s = MSMessage.toString(content, i, i + slen);
		    if (s.equalsIgnoreCase(seq))
			return i;
		}
	    }
	    int b = content[i] & 0xff;
	    bol = b == '\r' || b == '\n';
	}
	return -1;
    }
}
