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

import java.io.*;
import java.util.*;
import javax.mail.internet.MimeUtility;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test header folding.
 *
 * @author Bill Shannon
 */

@RunWith(Parameterized.class)
public class FoldTest {
    private String direction;
    private String orig;
    private String expect;

    private static List<Object[]> testData;

    public FoldTest(String direction, String orig, String expect) {
	this.direction = direction;
	this.orig = orig;
	this.expect = expect;
    }

    @Parameters
    public static Collection<Object[]> data() throws IOException {
	testData = new ArrayList<>();
	parse(new BufferedReader(new InputStreamReader(
	    FoldTest.class.getResourceAsStream("folddata"))));
	return testData;
    }

    /**
     * Read the data from the test file.  Format is multiple of any of
     * the following:
     *
     * FOLD\nString$\nEXPECT\nString$\n
     * UNFOLD\nString$\nEXPECT\nString$\n
     * BOTH\nString$\n
     */
    private static void parse(BufferedReader in) throws IOException {
	String line;
	while ((line = in.readLine()) != null) {
	    if (line.startsWith("#") || line.length() == 0)
		continue;
	    String orig = readString(in);
	    if (line.equals("BOTH")) {
		testData.add(new Object[] { line, orig, null });
	    } else {
		String e = in.readLine();
		if (!e.equals("EXPECT"))
		    throw new IOException("TEST DATA FORMAT ERROR");
		String expect = readString(in);
		testData.add(new Object[] { line, orig, expect });
	    }
	}
    }

    /**
     * Read a string that ends with '$', preserving all characters,
     * especially including CR and LF.
     */
    private static String readString(BufferedReader in) throws IOException {
	StringBuffer sb = new StringBuffer();
	int c;
	while ((c = in.read()) != '$')
	    sb.append((char)c);
	in.readLine();	// throw away rest of line
	return sb.toString();
    }

    @Test
    public void testFold() {
	if (direction.equals("BOTH")) {
	    String fs = MimeUtility.fold(0, orig);
	    String us = MimeUtility.unfold(fs);
	    Assert.assertEquals(orig, us);
	} else if (direction.equals("FOLD")) {
	    Assert.assertEquals("Fold", expect, MimeUtility.fold(0, orig));
	} else if (direction.equals("UNFOLD")) {
	    Assert.assertEquals("Unfold", expect, MimeUtility.unfold(orig));
	} else {
	    Assert.fail("Unknown direction: " + direction);
	}
    }
}
