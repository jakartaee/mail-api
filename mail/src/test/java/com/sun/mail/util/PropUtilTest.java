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

package com.sun.mail.util;

import java.util.Properties;
import javax.mail.Session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test that the PropUtil methods return the correct values,
 * especially when defaults and non-String values are considered.
 */
public class PropUtilTest {
    @Test
    public void testInt() throws Exception {
	Properties props = new Properties();
	props.setProperty("test", "2");
	assertEquals(PropUtil.getIntProperty(props, "test", 1), 2);
    }

    @Test
    public void testIntDef() throws Exception {
	Properties props = new Properties();
	assertEquals(PropUtil.getIntProperty(props, "test", 1), 1);
    }

    @Test
    public void testIntDefProp() throws Exception {
	Properties defprops = new Properties();
	defprops.setProperty("test", "2");
	Properties props = new Properties(defprops);
	assertEquals(PropUtil.getIntProperty(props, "test", 1), 2);
    }

    @Test
    public void testInteger() throws Exception {
	Properties props = new Properties();
	props.put("test", 2);
	assertEquals(PropUtil.getIntProperty(props, "test", 1), 2);
    }

    @Test
    public void testBool() throws Exception {
	Properties props = new Properties();
	props.setProperty("test", "true");
	assertTrue(PropUtil.getBooleanProperty(props, "test", false));
    }

    @Test
    public void testBoolDef() throws Exception {
	Properties props = new Properties();
	assertTrue(PropUtil.getBooleanProperty(props, "test", true));
    }

    @Test
    public void testBoolDefProp() throws Exception {
	Properties defprops = new Properties();
	defprops.setProperty("test", "true");
	Properties props = new Properties(defprops);
	assertTrue(PropUtil.getBooleanProperty(props, "test", false));
    }

    @Test
    public void testBoolean() throws Exception {
	Properties props = new Properties();
	props.put("test", true);
	assertTrue(PropUtil.getBooleanProperty(props, "test", false));
    }


    // the Session variants...

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionInt() throws Exception {
	Properties props = new Properties();
	props.setProperty("test", "2");
	Session sess = Session.getInstance(props, null);
	assertEquals(PropUtil.getIntSessionProperty(sess, "test", 1), 2);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionIntDef() throws Exception {
	Properties props = new Properties();
	Session sess = Session.getInstance(props, null);
	assertEquals(PropUtil.getIntSessionProperty(sess, "test", 1), 1);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionIntDefProp() throws Exception {
	Properties defprops = new Properties();
	defprops.setProperty("test", "2");
	Properties props = new Properties(defprops);
	Session sess = Session.getInstance(props, null);
	assertEquals(PropUtil.getIntSessionProperty(sess, "test", 1), 2);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionInteger() throws Exception {
	Properties props = new Properties();
	props.put("test", 2);
	Session sess = Session.getInstance(props, null);
	assertEquals(PropUtil.getIntSessionProperty(sess, "test", 1), 2);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionBool() throws Exception {
	Properties props = new Properties();
	props.setProperty("test", "true");
	Session sess = Session.getInstance(props, null);
	assertTrue(PropUtil.getBooleanSessionProperty(sess, "test", false));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionBoolDef() throws Exception {
	Properties props = new Properties();
	Session sess = Session.getInstance(props, null);
	assertTrue(PropUtil.getBooleanSessionProperty(sess, "test", true));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionBoolDefProp() throws Exception {
	Properties defprops = new Properties();
	defprops.setProperty("test", "true");
	Properties props = new Properties(defprops);
	Session sess = Session.getInstance(props, null);
	assertTrue(PropUtil.getBooleanSessionProperty(sess, "test", false));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testSessionBoolean() throws Exception {
	Properties props = new Properties();
	props.put("test", true);
	Session sess = Session.getInstance(props, null);
	assertTrue(PropUtil.getBooleanSessionProperty(sess, "test", false));
    }


    // the System variants...

    @Test
    public void testSystemBool() throws Exception {
	System.setProperty("test", "true");
	assertTrue(PropUtil.getBooleanSystemProperty("test", false));
    }

    @Test
    public void testSystemBoolDef() throws Exception {
	assertTrue(PropUtil.getBooleanSystemProperty("testnotset", true));
    }

    @Test
    public void testSystemBoolean() throws Exception {
	System.getProperties().put("testboolean", true);
	assertTrue(PropUtil.getBooleanSystemProperty("testboolean", false));
    }
}
