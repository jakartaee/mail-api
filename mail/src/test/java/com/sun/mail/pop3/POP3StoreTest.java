/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.mail.pop3;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import com.sun.mail.test.TestServer;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test POP3Store.
 *
 * @author sbo
 * @author Bill Shannon
 */
public final class POP3StoreTest {

    /**
     * Check is connected.
     */
    @Test
    public void testIsConnected() {
        TestServer server = null;
        try {
            final POP3Handler handler = new POP3HandlerNoopErr();
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.pop3.host", "localhost");
            properties.setProperty("mail.pop3.port", "" + server.getPort());
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Store store = session.getStore("pop3");
            try {
                store.connect("test", "test");
                final Folder folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);

                // Check
                assertFalse(folder.isOpen());
            } finally {
                store.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
            }
        }
    }

    /**
     * Check that enabling APOP with a server that doesn't support APOP
     * (and doesn't return any information in the greeting) doesn't fail.
     */
    @Test
    public void testApopNotSupported() {
        TestServer server = null;
        try {
            final POP3Handler handler = new POP3HandlerNoGreeting();
            server = new TestServer(handler);
            server.start();

            final Properties properties = new Properties();
            properties.setProperty("mail.pop3.host", "localhost");
            properties.setProperty("mail.pop3.port", "" + server.getPort());
            properties.setProperty("mail.pop3.apop.enable", "true");
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Store store = session.getStore("pop3");
            try {
                store.connect("test", "test");
		// success!
            } finally {
                store.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
            }
        }
    }

    /**
     * Custom handler. Returns ERR for NOOP.
     *
     * @author sbo
     */
    private static final class POP3HandlerNoopErr extends POP3Handler {

        /**
         * {@inheritDoc}
         */
	@Override
        public void noop() throws IOException {
            this.println("-ERR");
        }
    }

    /**
     * Custom handler.  Don't include any extra information in the greeting.
     *
     * @author Bill Shannon
     */
    private static final class POP3HandlerNoGreeting extends POP3Handler {

        /**
         * {@inheritDoc}
         */
	@Override
        public void sendGreetings() throws IOException {
            this.println("+OK");
        }
    }
}
