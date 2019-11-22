/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

public class TestResult {
    private static boolean pass = false;

    private TestResult() { }	// no public constructor

    public static void reset() {
	pass = false;
    }

    public static void success() {
	pass = true;
    }

    public static void print(String s) {
	System.out.println((pass ? "SUCCESS: " : "FAIL: ") + s);
    }
}
