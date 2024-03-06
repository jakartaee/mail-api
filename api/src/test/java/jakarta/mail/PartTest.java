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

import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.DummyStreamProvider;
import jakarta.mail.util.LineInputStream;
import jakarta.mail.util.LineOutputStream;
import jakarta.mail.util.StreamProvider;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.junit.Test;

public class PartTest {

    // Sessions with different StreamProviders
    private static final Session SESSION_DEFAULT = Session.getDefaultInstance(new Properties());
    private static final Session SESSION_1;
    private static final Session SESSION_2;

    static {
        SESSION_1 = Session.getDefaultInstance(new Properties());
        System.setProperty(StreamProvider.class.getName(), CustomStreamProvider.class.getName());
        SESSION_2 = Session.getInstance(new Properties());
        System.clearProperty(StreamProvider.class.getName());
    }

    @Test
    public void streamProvidersChecker() {
        assertEquals(DummyStreamProvider.class, SESSION_DEFAULT.getStreamProvider().getClass());
        assertEquals(DummyStreamProvider.class, SESSION_1.getStreamProvider().getClass());
        assertEquals(CustomStreamProvider.class, SESSION_2.getStreamProvider().getClass());
        assertEquals(SESSION_DEFAULT.getStreamProvider(), SESSION_1.getStreamProvider());
    }
    
    @Test
    public void sameInstance() throws MessagingException {
        MimeBodyPart body = new MimeBodyPart();
        MimeMultipart multiPart = new MimeMultipart();
        assertEquals(SESSION_DEFAULT.getStreamProvider(), body.getStreamProvider());
        assertEquals(SESSION_DEFAULT.getStreamProvider(), multiPart.getStreamProvider());
    }

    @Test
    public void specifySession1() throws MessagingException {
        Message message = new MimeMessage(SESSION_1);
        Multipart multipart = new MimeMultipart();
        message.setContent(multipart);
        assertEquals(SESSION_1.getStreamProvider(), multipart.getStreamProvider());
    }

    @Test
    public void specifySession2() throws MessagingException {
        Message message = new MimeMessage(SESSION_2);
        Multipart multipart = new MimeMultipart();
        message.setContent(multipart);
        assertEquals(SESSION_2.getStreamProvider(), multipart.getStreamProvider());
    }

    public static class CustomStreamProvider implements StreamProvider {

        @Override
        public InputStream inputBase64(InputStream in) {
            return null;
        }

        @Override
        public OutputStream outputBase64(OutputStream out) {
            return null;
        }

        @Override
        public InputStream inputBinary(InputStream in) {
            return null;
        }

        @Override
        public OutputStream outputBinary(OutputStream out) {
            return null;
        }

        @Override
        public OutputStream outputB(OutputStream out) {
            return null;
        }

        @Override
        public InputStream inputQ(InputStream in) {
            return null;
        }

        @Override
        public OutputStream outputQ(OutputStream out, boolean encodingWord) {
            return null;
        }

        @Override
        public LineInputStream inputLineStream(InputStream in, boolean allowutf8) {
            return null;
        }

        @Override
        public LineOutputStream outputLineStream(OutputStream out, boolean allowutf8) {
            return null;
        }

        @Override
        public InputStream inputQP(InputStream in) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public OutputStream outputQP(OutputStream out) {
            return null;
        }

        @Override
        public InputStream inputSharedByteArray(byte[] buff) {
            return null;
        }

        @Override
        public InputStream inputUU(InputStream in) {
            return null;
        }

        @Override
        public OutputStream outputUU(OutputStream out, String filename) {
            return null;
        }
    }
}
