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

package com.sun.mail.imap.protocol;

import java.io.*;
import java.util.*;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test UIDSet.
 *
 * @author Bill Shannon
 */

@RunWith(Parameterized.class)
public class UIDSetTest {
    private TestData data;

    private static boolean gen_test_input = false;	// output good
    private static int errors = 0;		// number of errors detected

    private static boolean junit;

    static class TestData {
	public String name;
	public String uids;
	public long max;
	public String maxuids;
	public long[] expect;
    }

    public UIDSetTest(TestData t) {
	data = t;
    }

    @Test
    public void testData() {
	test(data);
    }

    @Parameters
    public static Collection<TestData[]> data() throws Exception {
	junit = true;
	// XXX - gratuitous array requirement
	List<TestData[]> testData = new ArrayList<>();
	BufferedReader in = new BufferedReader(new InputStreamReader(
	    UIDSetTest.class.getResourceAsStream("uiddata")));
	TestData t;
	while ((t = parse(in)) != null)
	    testData.add(new TestData[] { t });
	return testData;
    }

    public static void main(String argv[]) throws Exception {
	int optind;
	// XXX - all options currently ignored
	for (optind = 0; optind < argv.length; optind++) {
	    if (argv[optind].equals("-")) {
		// ignore
	    } else if (argv[optind].equals("-g")) {
		gen_test_input = true;
	    } else if (argv[optind].equals("--")) {
		optind++;
		break;
	    } else if (argv[optind].startsWith("-")) {
		System.out.println(
		    "Usage: uidtest [-g] [-]");
		System.exit(1);
	    } else {
		break;
	    }
	}

	// read from stdin
	BufferedReader in =
	    new BufferedReader(new InputStreamReader(System.in));

	TestData t;
	while ((t = parse(in)) != null)
	    test(t);
	System.exit(errors);
    }

    /*
     * Parse the input, returning a test case.
     */
    public static TestData parse(BufferedReader in) throws Exception {

	String line = null;
	for (;;) {
	    line = in.readLine();
	    if (line == null)
		return null;
	    if (line.length() == 0 || line.startsWith("#"))
		continue;

	    if (!line.startsWith("TEST"))
		throw new Exception("Bad test data format");
	    break;
	}

	TestData t = new TestData();
	int i = line.indexOf(' ');	// XXX - crude
	t.name = line.substring(i + 1);

	line = in.readLine();
	StringTokenizer st = new StringTokenizer(line);
	String tok = st.nextToken();
	if (!tok.equals("DATA"))
	    throw new Exception("Bad test data format: " + line);
	tok = st.nextToken();
	if (tok.equals("NULL"))
	    t.uids = null;
	else if (tok.equals("EMPTY"))
	    t.uids = "";
	else
	    t.uids = tok;

	line = in.readLine();
	st = new StringTokenizer(line);
	tok = st.nextToken();
	if (tok.equals("MAX")) {
	    tok = st.nextToken();
	    try {
		t.max = Long.valueOf(tok);
	    } catch (NumberFormatException ex) {
		throw new Exception("Bad MAX value in line: " + line);
	    }
	    if (st.hasMoreTokens())
		t.maxuids = st.nextToken();
	    else
		t.maxuids = t.uids;
	    line = in.readLine();
	    st = new StringTokenizer(line);
	    tok = st.nextToken();
	}
	List<Long> uids = new ArrayList<>();
	if (!tok.equals("EXPECT"))
	    throw new Exception("Bad test data format: " + line);
	while (st.hasMoreTokens()) {
	    tok = st.nextToken();
	    if (tok.equals("NULL"))
		t.expect = null;
	    else if (tok.equals("EMPTY"))
		t.expect = new long[0];
	    else {
		try {
		    uids.add(Long.valueOf(tok));
		} catch (NumberFormatException ex) {
		    throw new Exception("Bad DATA option in line: " + line);
		}
	    }
	}
	if (uids.size() > 0) {
	    t.expect = new long[uids.size()];
	    i = 0;
	    for (Long l : uids)
		t.expect[i++] = l.longValue();
	}

	return t;
    }

    /**
     * Test the data in the test case.
     */
    public static void test(TestData t) {
	// XXX - handle nulls

	// first, test string to array
	UIDSet[] uidset = UIDSet.parseUIDSets(t.uids);
	long[] uids;
	if (t.max > 0)
	    uids = UIDSet.toArray(uidset, t.max);
	else
	    uids = UIDSet.toArray(uidset);
	if (junit)
	    Assert.assertArrayEquals(t.expect, uids);
	else if (!arrayEquals(t.expect, uids)) {
	    System.out.println("Test: " + t.name);
	    System.out.println("FAIL");
	    errors++;
	}

	// now, test the reverse
	UIDSet[] uidset2 = UIDSet.createUIDSets(uids);
	String suid = UIDSet.toString(uidset2);
	String euid = t.max > 0 ? t.maxuids : t.uids;
	if (junit)
	    Assert.assertEquals(euid, suid);
	else if (!euid.equals(suid)) {
	    System.out.println("Test: " + t.name);
	    System.out.println("FAIL2");
	    errors++;
	}
    }

    private static boolean arrayEquals(long[] a,long[] b) {
	if (a == b)
	    return true;
	if (a == null || b == null)
	    return false;
	if (a.length != b.length)
	    return false;
	for (int i = 0; i < a.length; i++)
	    if (a[i] != b[i])
		return false;
	return true;
    }
}
