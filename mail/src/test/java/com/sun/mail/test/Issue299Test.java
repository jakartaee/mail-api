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

package com.sun.mail.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.junit.Test;


public class Issue299Test {

    static final int PORT = 18465;
    static final String USER = "xxxunj******@163.com";
    static final String PASSWD = "EY**************";
    static final String toMail = "甲申申甶甴甸电甹甸甸畀畱畱瘮畣畯畭甾瘍瘊畄畁畔畁瘍瘊畓畵畢番略畣畴町畐畗畎畅畄瘍瘊瘍瘊畉瘠界畏畖畅瘠留畏畕瘡瘍瘊瘮瘍瘊畑畕畉畔瘍瘊@qq.com";
    static String TEXT = "Hello world";
    static String SUBJECT = "Test";

    static Properties properties = new Properties();
    static Authenticator authenticator = null;

    public static void initProp(){
        properties.setProperty("mail.host","127.0.0.1");
        properties.put("mail.debug", "true");
        properties.setProperty("mail.transport.protocol","smtp");
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.timeout", "1000");
        properties.setProperty("mail.smtp.port", Integer.toString(PORT));
        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASSWD);
            }
        };
    }
    public static Session getSession(){
        return Session.getInstance(properties, authenticator);
    }

    @Test
    public void test() throws Exception {
        FakeSMTPServer server =  new FakeSMTPServer();
        ExecutorService service = Executors.newFixedThreadPool(1);
        try {
            service.execute(() -> {
                try {
                    server.start(PORT);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    
            initProp();
    
            Session session = getSession();
            MimeMessage msg = new MimeMessage(session);
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(toMail));
            msg.setSubject(SUBJECT);
            msg.setContent(TEXT, "text/html;charset=utf-8");
            msg.saveChanges();
            Transport.send(msg);
            fail("It is expected an IllegalArgumentException because there are illegal characters in the command");
        } catch (Exception e) {
            if (e.getCause() == null || e.getCause().getClass() != IllegalArgumentException.class) {
                fail("It is expected an IllegalArgumentException because there are illegal characters in the command. Exception was: " + e);
            }
        } finally {
            server.stop();
            service.shutdown();
            service.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

}