/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.util;

import java.io.*;
import java.util.*;
import javax.mail.*;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test base64 encoding/decoding.
 *
 * @author Bill Shannon
 */

public class BASE64Test {

    @Test
    public void test() throws IOException {
	// test a range of buffer sizes
	for (int bufsize = 1; bufsize < 100; bufsize++) {
	    //System.out.println("Buffer size: " + bufsize);
	    byte[] buf = new byte[bufsize];

	    // test a set of patterns

	    // first, all zeroes
	    Arrays.fill(buf, (byte)0);
	    test("Zeroes", buf);

	    // now, all ones
	    Arrays.fill(buf, (byte)0xff);
	    test("Ones", buf);

	    // now, small integers
	    for (int i = 0; i < bufsize; i++)
		buf[i] = (byte)i;
	    test("Ints", buf);

	    // finally, random numbers
	    Random rnd = new Random();
	    rnd.nextBytes(buf);
	    test("Random", buf);
	}
    }

    /**
     * Encode and decode the buffer and check that we get back the
     * same data.  Encoding is done both with the static encode
     * method and using the encoding stream.  Likewise, decoding
     * is done both with the static decode method and using the
     * decoding stream.  Check all combinations.
     */
    private static void test(String name, byte[] buf) throws IOException {
	// first encode and decode with method
	byte[] encoded = BASE64EncoderStream.encode(buf);
	byte[] nbuf = BASE64DecoderStream.decode(encoded);
	compare(name, "method", buf, nbuf);

	// encode with stream, compare with method encoded version
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	BASE64EncoderStream os =
	    new BASE64EncoderStream(bos, Integer.MAX_VALUE);
	os.write(buf);
	os.flush();
	os.close();
	byte[] sbuf = bos.toByteArray();
	compare(name, "encoded", encoded, sbuf);

	// encode with stream, decode with method
	nbuf = BASE64DecoderStream.decode(sbuf);
	compare(name, "stream->method", buf, nbuf);

	// encode with stream, decode with stream
	ByteArrayInputStream bin = new ByteArrayInputStream(sbuf);
	BASE64DecoderStream in = new BASE64DecoderStream(bin);
	readAll(in, nbuf, nbuf.length);
	compare(name, "stream", buf, nbuf);

	// encode with method, decode with stream
	for (int i = 1; i <= nbuf.length; i++) {
	    bin = new ByteArrayInputStream(encoded);
	    in = new BASE64DecoderStream(bin);
	    readAll(in, nbuf, i);
	    compare(name, "method->stream " + i, buf, nbuf);
	}

	// encode with stream, decode with stream, many buffers

	// first, fill the output with multiple buffers, up to the limit
	int limit = 10000;		// more than 8K
	bos = new ByteArrayOutputStream();
	os = new BASE64EncoderStream(bos);
	for (int size = 0, blen = buf.length; size < limit; size += blen) {
	    if (size + blen > limit) {
		blen = limit - size;
		// write out partial buffer, starting at non-zero offset
		os.write(buf, buf.length - blen, blen);
	    } else
		os.write(buf);
	}
	os.flush();
	os.close();

	// read the encoded output and check the line length
	String type = "big stream";		// for error messages below
	sbuf = bos.toByteArray();
	bin = new ByteArrayInputStream(sbuf);
	byte[] inbuf = new byte[78];
	for (int size = 0, blen = 76; size < limit; size += blen) {
	    if (size + blen > limit) {
		blen = limit - size;
		int n = bin.read(inbuf, 0, blen);
		Assert.assertEquals(name + ": " + type +
		    " read wrong size at offset " + (size + blen), blen, n);
	    } else {
		int n = bin.read(inbuf, 0, blen + 2);
		Assert.assertEquals(name + ": " + type +
		    " read wrong size at offset " + (size + blen), blen + 2, n);
		Assert.assertTrue(name + ": " + type +
		    " no CRLF: at offset " + (size + blen),
		    inbuf[blen] == (byte)'\r' && inbuf[blen+1] == (byte)'\n');
	    }
	}

	// decode the output and check the data
	bin = new ByteArrayInputStream(sbuf);
	in = new BASE64DecoderStream(bin);
	inbuf = new byte[buf.length];
	for (int size = 0, blen = buf.length; size < limit; size += blen) {
	    if (size + blen > limit)
		blen = limit - size;
	    int n = in.read(nbuf, 0, blen);
	    Assert.assertEquals(name + ": " + type +
		" read decoded wrong size at offset " + (size + blen), blen, n);
	    if (blen != buf.length) {
		// have to compare with end of original buffer
		byte[] cbuf = new byte[blen];
		System.arraycopy(buf, buf.length - blen, cbuf, 0, blen);
		// need a version of the read buffer that's the right size
		byte[] cnbuf = new byte[blen];
		System.arraycopy(nbuf, 0, cnbuf, 0, blen);
		compare(name, type, cbuf, cnbuf);
	    } else {
		compare(name, type, buf, nbuf);
	    }
	}
    }

    private static byte[] origLine;
    private static byte[] encodedLine;
    static {
	try {
	    origLine =
		"000000000000000000000000000000000000000000000000000000000".
		    getBytes("us-ascii");
	    encodedLine =
		("MDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAw" +
		"MDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAw" + "\r\n").
		    getBytes("us-ascii");
	} catch (UnsupportedEncodingException uex) {
	    // should never happen;
	}
    }

    /**
     * Test that CRLF is inserted at the right place.
     * Test combinations of array writes of different sizes
     * and single byte writes.
     */
    @Test
    public void testLineLength() throws Exception {
	for (int i = 0; i < origLine.length; i++) {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    OutputStream os = new BASE64EncoderStream(bos);
	    os.write(origLine, 0, i);
	    os.write(origLine, i, origLine.length - i);
	    os.write((byte)'0');
	    os.flush();
	    os.close();

	    byte[] line = new byte[encodedLine.length];
	    System.arraycopy(bos.toByteArray(), 0, line, 0, line.length);
	    Assert.assertArrayEquals("encoded line " + i, encodedLine, line);
	}

	for (int i = 0; i < origLine.length; i++) {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    OutputStream os = new BASE64EncoderStream(bos);
	    os.write(origLine, 0, i);
	    os.write(origLine, i, origLine.length - i);
	    os.write(origLine);
	    os.flush();
	    os.close();

	    byte[] line = new byte[encodedLine.length];
	    System.arraycopy(bos.toByteArray(), 0, line, 0, line.length);
	    Assert.assertArrayEquals("all arrays, encoded line " + i,
					encodedLine, line);
	}

	for (int i = 1; i < 5; i++) {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    OutputStream os = new BASE64EncoderStream(bos);
	    for (int j = 0; j < i; j++)
		os.write((byte)'0');
	    os.write(origLine, i, origLine.length - i);
	    os.write((byte)'0');
	    os.flush();
	    os.close();

	    byte[] line = new byte[encodedLine.length];
	    System.arraycopy(bos.toByteArray(), 0, line, 0, line.length);
	    Assert.assertArrayEquals("single byte first encoded line " + i,
					encodedLine, line);
	}
	for (int i = origLine.length - 5; i < origLine.length; i++) {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    OutputStream os = new BASE64EncoderStream(bos);
	    os.write(origLine, 0, i);
	    for (int j = 0; j < origLine.length - i; j++)
		os.write((byte)'0');
	    os.write((byte)'0');
	    os.flush();
	    os.close();

	    byte[] line = new byte[encodedLine.length];
	    System.arraycopy(bos.toByteArray(), 0, line, 0, line.length);
	    Assert.assertArrayEquals("single byte last encoded line " + i,
					encodedLine, line);
	}
    }

    @Test
    public void testReadZeroBytes() throws Exception {
	byte[] decoded = new byte[10000];
	for (int i = 0; i < 1000; i++)
		decoded[i] = (byte)'A';
	byte[] encoded = java.util.Base64.getEncoder().encode(decoded);
	// Exceed InputStream.DEFAULT_BUFFER_SIZE
	assertTrue(decoded.length > 8192);
	BASE64DecoderStream sut =
		new BASE64DecoderStream(new ByteArrayInputStream(encoded));
	// XXX - should test this using something equivalent to JDK 9's
	// InputStream.readAllBytes, but for now...
	int n = sut.read(decoded, 0, 0);
	assertEquals(n, 0);

	// Exercise
	//byte[] result = sut.readAllBytes();
	// Verify
	//assertArrayEquals(decoded, result);
    }

    /**
     * Fill the buffer from the stream.
     */
    private static void readAll(InputStream in, byte[] buf, int readsize)
				throws IOException {
	int need = buf.length;
	int off = 0; 
	int got;
	while (need > 0) {
	    got = in.read(buf, off, need > readsize ? readsize : need);
	    if (got <= 0)
		break;
	    off += got;
	    need -= got;
	}
	if (need != 0)
	    System.out.println("couldn't read all bytes");
    }

    /**
     * Compare the two buffers.
     */
    private static void compare(String name, String type,
				byte[] buf, byte[] nbuf) {
	/*
	if (nbuf.length != buf.length) {
	    System.out.println(name + ": " + type +
		" decoded array size wrong: " +
		"got " + nbuf.length + ", expected " + buf.length);
	    dump(name + " buf", buf);
	    dump(name + " nbuf", nbuf);
	}
	*/
	Assert.assertEquals(name + ": " + type + " decoded array size wrong",
			    buf.length, nbuf.length);
	for (int i = 0; i < buf.length; i++) {
	    Assert.assertEquals(name + ": " + type + " data wrong: index " + i,
		buf[i], nbuf[i]);
	}
    }

    /**
     * Dump the contents of the buffer.
     */
    private static void dump(String name, byte[] buf) {
	System.out.println(name);
	for (int i = 0; i < buf.length; i++)
	    System.out.println(buf[i]);
    }
}
