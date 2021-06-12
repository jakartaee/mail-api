/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Properties;
import java.util.UUID;
import jakarta.mail.Session;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniqueValueTest {
    
    private static final String MESSAGEID_KEY = "mail.mime.messageid.format";
    
    private static final String BOUNDARY_KEY 
            = "mail.mime.multipart.boundary.format";

    public UniqueValueTest() {
    }
    
    @Test(expected = java.lang.IllegalAccessException.class)
    public void testDeclaredConstructor() throws ReflectiveOperationException {
        UniqueValue.class.getDeclaredConstructor().newInstance();
    }

    @Test
    public void testDefGetUniqueBoundaryValue() {
        String v = System.getProperty(BOUNDARY_KEY);
        assertNull(v, v);
        expectUuidBoundary();
    }
    
    private void expectUuidBoundary() {
        String start = "----=_Part_";
        String result = UniqueValue.getUniqueBoundaryValue();
        assertTrue(result, result.startsWith(start));
        try {
            UUID.fromString(result.substring(start.length()));
        } catch (Throwable t) {
            throw new RuntimeException(result, t);
        }
    }

    @Test
    public void testInvalidUuidGetUniqueBoundaryValue() {
        System.setProperty(BOUNDARY_KEY, UniqueValueTest.class.getName());
        try {
            expectUuidBoundary();
        } finally {
            System.getProperties().remove(BOUNDARY_KEY);
        }
    }
    
    @Test
    public void testUuidGetUniqueBoundaryValue() {
        System.setProperty(BOUNDARY_KEY, "true");
        try {
            expectUuidBoundary();
        } finally {
            System.getProperties().remove(BOUNDARY_KEY);
        }
    }

    @Test
    public void testUvGetUniqueBoundaryValue() {
        System.setProperty(BOUNDARY_KEY, "false");
        try {
            String start = "----=_Part_";
            String result = UniqueValue.getUniqueBoundaryValue();
            assertTrue(result, result.startsWith(start));
            try {
                int s = start.length();
                int n = result.indexOf('_', s);
                Long.parseLong(result.substring(s, n)); //id

                s = n + 1;
                n = result.indexOf('.', s);
                Long.parseLong(result.substring(s, n)); //hashCode

                Long.parseLong(result.substring(n + 1)); //millis
            } catch (Throwable t) {
                throw new RuntimeException(result, t);
            }
        } finally {
            System.getProperties().remove(BOUNDARY_KEY);
        }
    }

    @Test
    public void testDefGetUniqueMessageIDValue() {
        Properties p = new Properties();
        String v = p.getProperty(MESSAGEID_KEY);
        assertNull(v, v);
        expectUuidMessageID(Session.getInstance(p));
    }

    /**
     * If a session is given but the value is not defined ensure code will not
     * fallback to using system properties.
     */
    @Test
    public void testUuidNoheritGetUniqueMessageIDValue() {
        System.setProperty(MESSAGEID_KEY, "false");
        try {
            Properties p = new Properties();
            String v = p.getProperty(MESSAGEID_KEY);
            assertNull(v, v);
            expectUuidMessageID(Session.getInstance(p));
        } finally {
            System.getProperties().remove(MESSAGEID_KEY);
        }
    }
    

    @Test
    public void testUvGetUniqueMessageIDValue() {
        Properties p = new Properties();
        p.put(MESSAGEID_KEY, "false");
        String result = UniqueValue.getUniqueMessageIDValue(
                Session.getInstance(p));
        try {
            int s = 0;
            int n = result.indexOf('.', s);
            Long.parseLong(result.substring(s, n)); //id

            s = n + 1;
            n = result.indexOf('.', s);
            Long.parseLong(result.substring(s, n)); //hashCode

            s = n + 1;
            n = result.indexOf('@', s);
            Long.parseLong(result.substring(s, n)); //millis
        } catch (Throwable t) {
            throw new RuntimeException(result, t);
        }
    }

    @Test
    public void testUuidGetUniqueMessageIDValue() {
        Properties p = new Properties();
        p.put(MESSAGEID_KEY, "true");
        expectUuidMessageID(Session.getInstance(p));
    }

    @Test
    public void testDefSystemGetUniqueMessageIDValue() {
        String v = System.getProperty(MESSAGEID_KEY);
        assertNull(v, v);
        expectUuidMessageID((Session) null);
    }
    
    @Test
    public void testInvalidGetUniqueMessageIDValue() {
        Properties p = new Properties();
        p.put(MESSAGEID_KEY, UniqueValueTest.class.getName());
        expectUuidMessageID(Session.getInstance(p));
    }
    
    private void expectUuidMessageID(Session s) {
        String result = UniqueValue.getUniqueMessageIDValue(s);
        try {
            UUID.fromString(result.substring(0, result.indexOf('@')));
        } catch (Throwable t) {
            throw new RuntimeException(result, t);
        }
    }
    
    @Test
    public void testUuidSystemGetUniqueMessageIDValue() {        
        System.setProperty(MESSAGEID_KEY, "true");
        try {
            expectUuidMessageID((Session) null);
        } finally {
            System.getProperties().remove(MESSAGEID_KEY);
        }
    }

    @Test
    public void testUvSystemGetUniqueMessageIDValue() {
        System.setProperty(MESSAGEID_KEY, "false");
        try {
            String result = UniqueValue.getUniqueMessageIDValue((Session) null);
            try {
                int s = 0;
                int n = result.indexOf('.', s);
                Long.parseLong(result.substring(s, n)); //id

                s = n + 1;
                n = result.indexOf('.', s);
                Long.parseLong(result.substring(s, n)); //hashCode

                s = n + 1;
                n = result.indexOf('@', s);
                Long.parseLong(result.substring(s, n)); //millis
            } catch (Throwable t) {
                throw new RuntimeException(result, t);
            }
        } finally {
            System.getProperties().remove(MESSAGEID_KEY);
        }
    }
}
