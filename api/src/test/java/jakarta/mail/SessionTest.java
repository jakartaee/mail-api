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

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

import jakarta.mail.Provider.Type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionTest {

    @Test
    public void issue527And812() throws NoSuchProviderException {
        Session session = Session.getInstance(new Properties());
        Stream<Provider> providerStream = Arrays.stream(session.getProviders());
        // test exists in both jakarta.providers and javamail.providers
        long count = providerStream.filter(p -> "test".equals(p.getProtocol())).count();
        assertEquals(2, count);
        Provider provider = session.getProvider("test");
        // javamail.providers one has precedence in case of conflict
        assertEquals(JavaMail.class.getName(), provider.getClassName());
        provider = session.getProvider("test2");
        assertEquals(Jakarta.class.getName(), provider.getClassName());
        provider = session.getProvider("test3");
        assertEquals(JavaMail.class.getName(), provider.getClassName());
    }

    @Test
    public void byProperty() throws NoSuchProviderException {
        Properties prop = new Properties();
        prop.put("mail.test4.class", Jakarta.class.getName());
        Session session = Session.getInstance(prop);
        // protocol test4 exists in both with same class, but javamail.providers has precedence
        Provider provider = session.getProvider("test4");
        assertEquals("OracleMail", provider.getVendor());
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
