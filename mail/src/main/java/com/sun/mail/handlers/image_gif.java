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
import java.awt.*;
import javax.activation.*;

/**
 * DataContentHandler for image/gif.
 */
public class image_gif extends handler_base {
    private static ActivationDataFlavor[] myDF = {
	new ActivationDataFlavor(Image.class, "image/gif", "GIF Image")
    };

    @Override
    protected ActivationDataFlavor[] getDataFlavors() {
	return myDF;
    }

    @Override
    public Object getContent(DataSource ds) throws IOException {
	InputStream is = ds.getInputStream();
	int pos = 0;
	int count;
	byte buf[] = new byte[1024];

	while ((count = is.read(buf, pos, buf.length - pos)) != -1) {
	    pos += count;
	    if (pos >= buf.length) {
		int size = buf.length;
		if (size < 256*1024)
		    size += size;
		else
		    size += 256*1024;
		byte tbuf[] = new byte[size];
		System.arraycopy(buf, 0, tbuf, 0, pos);
		buf = tbuf;
	    }
	}
	Toolkit tk = Toolkit.getDefaultToolkit();
	return tk.createImage(buf, 0, pos);
    }

    /**
     * Write the object to the output stream, using the specified MIME type.
     */
    @Override
    public void writeTo(Object obj, String type, OutputStream os)
			throws IOException {
	if (!(obj instanceof Image))
	    throw new IOException("\"" + getDataFlavors()[0].getMimeType() +
		"\" DataContentHandler requires Image object, " +
		"was given object of type " + obj.getClass().toString());

	throw new IOException(getDataFlavors()[0].getMimeType() +
				" encoding not supported");
    }
}
