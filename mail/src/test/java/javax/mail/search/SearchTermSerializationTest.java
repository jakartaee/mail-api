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

package javax.mail.search;

import java.io.*;
import java.util.Date;

import javax.mail.*;
import javax.mail.search.*;
import javax.mail.internet.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * SearchTerm serialization test.
 *
 * @author Bill Shannon
 */

public class SearchTermSerializationTest {

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
	// construct a SearchTerm using all SearchTerm types
	SearchTerm term = new AndTerm(new SearchTerm[] {
	    new BodyTerm("text"),
	    new FlagTerm(new Flags(Flags.Flag.RECENT), true),
	    new FromStringTerm("foo@bar"),
	    new HeaderTerm("X-Mailer", "dtmail"),
	    new MessageIDTerm("12345@sun.com"),
	    new MessageNumberTerm(42),
	    new NotTerm(
		new OrTerm(
		    new ReceivedDateTerm(ReceivedDateTerm.LT, new Date()),
		    new RecipientStringTerm(Message.RecipientType.CC, "foo")
		)
	    ),
	    new RecipientTerm(MimeMessage.RecipientType.NEWSGROUPS,
				new NewsAddress("comp.lang.java", "newshost")),
	    new SentDateTerm(SentDateTerm.NE, new Date()),
	    new SizeTerm(SizeTerm.LT, 1000),
	    new SubjectTerm("test")
	});

	// serialize it to a byte array
	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(bos);
	oos.writeObject(term);
	bos.close();

	// read it back in
	ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
	ObjectInputStream ois = new ObjectInputStream(bis);
	SearchTerm term2 = (SearchTerm)ois.readObject();

	// compare it with the original
	assertEquals(term, term2);
    }
}
