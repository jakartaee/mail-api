/*
 * Copyright (c) 1997, 2023 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Test header folding.
 *
 * @author Bill Shannon
 */

public class FoldTest {

    private static List<Arguments> testData;

    public static Stream<Arguments> data() throws IOException {
        testData = new ArrayList<>();
        parse(new BufferedReader(new InputStreamReader(
                FoldTest.class.getResourceAsStream("folddata"))));
        return testData.stream();
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
                testData.add(arguments(line, orig, null));
            } else {
                String e = in.readLine();
                if (!e.equals("EXPECT"))
                    throw new IOException("TEST DATA FORMAT ERROR");
                String expect = readString(in);
                testData.add(arguments(line, orig, expect));
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
            sb.append((char) c);
        in.readLine();    // throw away rest of line
        return sb.toString();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testFold(String direction, String orig, String expect) {
        if (direction.equals("BOTH")) {
            String fs = MimeUtility.fold(0, orig);
            String us = MimeUtility.unfold(fs);
            Assertions.assertEquals(orig, us);
        } else if (direction.equals("FOLD")) {
            Assertions.assertEquals(expect, MimeUtility.fold(0, orig), "Fold");
        } else if (direction.equals("UNFOLD")) {
            Assertions.assertEquals(expect, MimeUtility.unfold(orig), "Unfold");
        } else {
            Assertions.fail("Unknown direction: " + direction);
        }
    }
}
