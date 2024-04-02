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

package jakarta.mail;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

public class FolderTest {

    @Test
    public void closeIdempotent() throws MessagingException {
        CustomFolder folder = new CustomFolder(new DummyStore(Session.getDefaultInstance(new Properties()), null));
        folder.close();
        folder.close();
        assertEquals(1, folder.closedTimes);
    }

    private static class CustomFolder extends Folder {

        private int closedTimes = 0;

        protected CustomFolder(Store store) {
            super(store);
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getFullName() {
            return null;
        }

        @Override
        public Folder getParent() throws MessagingException {
            return null;
        }

        @Override
        public boolean exists() throws MessagingException {
            return false;
        }

        @Override
        public Folder[] list(String pattern) throws MessagingException {
            return null;
        }

        @Override
        public char getSeparator() throws MessagingException {
            return 0;
        }

        @Override
        public int getType() throws MessagingException {
            return 0;
        }

        @Override
        public boolean create(int type) throws MessagingException {
            return false;
        }

        @Override
        public boolean hasNewMessages() throws MessagingException {
            return false;
        }

        @Override
        public Folder getFolder(String name) throws MessagingException {
            return null;
        }

        @Override
        public boolean delete(boolean recurse) throws MessagingException {
            return false;
        }

        @Override
        public boolean renameTo(Folder f) throws MessagingException {
            return false;
        }

        @Override
        public void open(int mode) throws MessagingException {
        }

        @Override
        public void close(boolean expunge) throws MessagingException {
            closedTimes++;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public Flags getPermanentFlags() {
            return null;
        }

        @Override
        public int getMessageCount() throws MessagingException {
            return 0;
        }

        @Override
        public Message getMessage(int msgnum) throws MessagingException {
            return null;
        }

        @Override
        public void appendMessages(Message[] msgs) throws MessagingException {
        }

        @Override
        public Message[] expunge() throws MessagingException {
            return null;
        }
    }

    private static class DummyStore extends Store {

        protected DummyStore(Session session, URLName urlname) {
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
