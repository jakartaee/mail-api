/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.Test;

public class MimeMessageTest {

    @Test
    public void dateTimeFormatter() throws ParseException {
        MailDateFormat mailDateFormat1 = new MailDateFormat();
        DateTimeFormatter mailDateFormat2 = MimeMessage.mailDateFormat;
        Date date = new Date(1341100798000L);
        String s1 = mailDateFormat1.format(date);
        String s2 = mailDateFormat2.format(date.toInstant());
        assertEquals(s1, s2);
        Date d1 = mailDateFormat1.parse(s1);
        Date d2 = Date.from(Instant.from(mailDateFormat2.parse(s1)));
        assertEquals(d1, d2);
    }
}
