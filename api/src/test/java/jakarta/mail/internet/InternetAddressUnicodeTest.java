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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Exercise the InternetAddress parser against an address list used copied from
 * Apache James' MailAddressTest, and additions from 
 * https://uasg.tech/download/uasg-004-use-cases-for-ua-readiness-evaluation-en/
 */
@RunWith(Parameterized.class)
public class InternetAddressUnicodeTest {

    private final String address;
    private final boolean good;

    public InternetAddressUnicodeTest(String address, boolean good) {
        this.address = address;
        this.good = good;
    }

    @BeforeClass
    public static void enableUtf8() {
        System.setProperty("mail.mime.allowutf8", "true");
    }

    @AfterClass
    public static void disableUtf8() {
        System.clearProperty("mail.mime.allowutf8");
    }

    private static final String[] GOOD = {
            "server-dev@james.apache.org",
            "\"quoted@local part\"@james.apache.org",
            "server-dev@james-apache.org",
            "local-part+details@example.com",
            "a&b@james-apache.org",
            "+details@example.com",
            "server-dev@[127.0.0.1]",
            "server.dev@james.apache.org",
            "\\.server-dev@james.apache.org",
            "Abc@10.42.0.1",
            "Abc.123@example.com",
            "Loïc.Accentué@voilà.fr8",
            "pelé@exemple.com",
            "δοκιμή@παράδειγμα.δοκιμή",
            "我買@屋企.香港",
            "二ノ宮@黒川.日本",
            "медведь@с-балалайкой.рф",
            "संपर्क@डाटामेल.भारत",
            "user+mailbox/department=shipping@example.com",
            "user+mailbox@example.com",
            "\"Abc@def\"@example.com",
            "\"Fred Bloggs\"@example.com",
            "\"Joe.\\Blow\"@example.com",
            "!#$%&'*+-/=?^_`.{|}~@example.com",
            // Extras, for good taste:
            "plain@example.com",
            "UPPER@EXAMPLE.COM",
            "x@y.z",
            "postmaster@example.com",
    };

    // Only addresses that InternetAddress itself rejects. The james test
    // lists many more, but some of them are rejected by the higher-level
    // James MailAddress validator rather than by jakarta's InternetAddress.
    private static final String[] BAD = {
            "",
            "server-dev@",
            "quoted local-part@james.apache.org",
            "quoted@local-part@james.apache.org",
            "server-dev@james. apache.org",
            "@example.com",
            "foo@",
            "foo@@example.com",
            "foo bar@example.com",
    };

    private static final String[] UASG_GOOD = {
            "info1@ua-test.link",
            "info2@ua-test.technology",
            "info3@普遍接受-测试.top",
            "info4@ua-test.世界",
            "info5@普遍接受-测试.世界",
            "info4@ua-test.xn--rhqv96g",
            "info3@xn----f38am99bqvcd5liy1cxsg.top",
            "info5@xn----f38am99bqvcd5liy1cxsg.xn--rhqv96g",
            // Devanagari local part + Devanagari domain:
            "uasg.tech@डाटामेल.भारत",
            "युएअसजी@डाटामेल.भारत",
            // CJK local part variants:
            "测试1@ua-test.link",
            "测试2@ua-test.technology",
            "测试3@普遍接受-测试.top",
            "测试4@ua-test.世界",
            "测试5@普遍接受-测试.世界",
            "测试4@ua-test.xn--rhqv96g",
            "测试3@xn----f38am99bqvcd5liy1cxsg.top",
            "测试5@xn----f38am99bqvcd5liy1cxsg.xn--rhqv96g",
            // Arabic labels (bidi marks stripped):
            "info6@ختبار-القبواللعالمي.top",
            "测试6@ختبار-القبواللعالمي.top",
            "user@السعودية.رسيل",
            "السعودية.دون@رسيل",
    };

    // UASG-004 addresses that the parser must still reject: they use
    // U+3002 IDEOGRAPHIC FULL STOP in place of '.', which java.net.IDN
    // happens to accept as a label separator but which is not a mailbox
    // dot under RFC 5321/5322/6531. Keep these on the reject path.
    //
    // Accepting U+3002 when a user enters an email address and replacing it with
    // an ASCII dot before sending it on makes sense. But this code cannot do that,
    // it has to simply reject an address containing the dot.
    private static final String[] UASG_BAD = {
            "info5@普遍接受-测试。世界",
            "测试5@普遍接受-测试。世界",
    };

    @Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        List<Object[]> rows = new ArrayList<>(
                GOOD.length + BAD.length + UASG_GOOD.length + UASG_BAD.length);
        for (String a : GOOD) {
            rows.add(new Object[]{a, true});
        }
        for (String a : UASG_GOOD) {
            rows.add(new Object[]{a, true});
        }
        for (String a : BAD) {
            rows.add(new Object[]{a, false});
        }
        for (String a : UASG_BAD) {
            rows.add(new Object[]{a, false});
        }
        return rows;
    }

    @Test
    public void parse() {
        if (good) {
            try {
                InternetAddress ia = new InternetAddress(address);
                assertNotNull(ia);
            } catch (AddressException e) {
                fail("expected " + address + " to parse, got: " + e.getMessage());
            }
        } else {
            try {
                new InternetAddress(address);
                fail("expected " + address + " to be rejected");
            } catch (AddressException e) {
                // expected
            }
        }
    }
}
