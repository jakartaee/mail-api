/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail;

import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionTest {

    @Test
    public void issue527() throws NoSuchProviderException {
        Session session = Session.getInstance(new Properties());
        Provider provider = session.getProvider("test");
        assertEquals(Jakarta.class.getName(), provider.getClassName());
    }

    public static class Jakarta extends Store {

        protected Jakarta(Session session, URLName urlname) {
            super(session, urlname);
        }

        @Override
        public Folder getDefaultFolder() throws MessagingException {
            return null;
        }

        @Override
        public Folder getFolder(String name) throws MessagingException {
            return null;
        }

        @Override
        public Folder getFolder(URLName url) throws MessagingException {
            return null;
        }
    }

    public static class JavaMail extends Store {

        protected JavaMail(Session session, URLName urlname) {
            super(session, urlname);
        }

        @Override
        public Folder getDefaultFolder() throws MessagingException {
            return null;
        }

        @Override
        public Folder getFolder(String name) throws MessagingException {
            return null;
        }

        @Override
        public Folder getFolder(URLName url) throws MessagingException {
            return null;
        }
    }
}
