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

package com.sun.mail.smtp;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test that the socket is closed when connect times out.
 */
public final class SMTPCloseTest {

    // timeout the test in case of failure
    @Rule
    public Timeout deadlockTimeout = Timeout.seconds(5);

    @Test
    public void test() {
        NopServer server = null;
        try {
            server = new NopServer();
            server.start();
            Thread.sleep(1000);

            final Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "localhost");
            properties.setProperty("mail.smtp.port", "" + server.getPort());
            properties.setProperty("mail.smtp.timeout", "100");
            final Session session = Session.getInstance(properties);
            //session.setDebug(true);

            final Transport t = session.getTransport("smtp");
            try {
                t.connect();
	    } catch (Exception ex) {
		// expect an exception when connect times out
            } finally {
                t.close();
            }
	    // give the server thread a chance to detect the close
	    for (int i = 0; i < 10 && !server.eof(); i++)
		Thread.sleep(100);
	    assertTrue("socket closed", server.eof());
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            if (server != null) {
                server.quit();
		server.interrupt();
            }
        }
    }
}
