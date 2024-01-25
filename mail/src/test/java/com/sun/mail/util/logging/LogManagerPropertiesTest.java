/*
 * Copyright (c) 2009, 2024 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2009, 2024 Jason Mehrens. All rights reserved.
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

package com.sun.mail.util.logging;

import java.io.*;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.*;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test case for the LogManagerProperties spec.
 *
 * @author Jason Mehrens
 */
public class LogManagerPropertiesTest extends AbstractLogging {

    /**
     * Holder used to inject Throwables into other APIs.
     */
    private final static ThreadLocal<Throwable> PENDING = new ThreadLocal<>();

    private static void fullFence() {
        LogManager.getLogManager().getProperty("");
    }

    private static void assumeNoJit() {
        CompilationMXBean c = ManagementFactory.getCompilationMXBean();
        if (c != null) { //-Xint
            Assume.assumeNoException(new IllegalArgumentException(
                    c.getName() + " must be disabled."));
        }
    }

    @Before
    public void setUp() {
        fullFence();
    }

    @After
    public void tearDown() {
        fullFence();
    }

    @Test
    public void testDeclaredClasses() throws Exception {
        Class<?>[] declared = LogManagerProperties.class.getDeclaredClasses();
        assertEquals(Arrays.toString(declared), 0, declared.length);
    }

    @Test
    public void testCheckLogManagerAccess() {
        try {
            LogManagerProperties.checkLogManagerAccess();
            //okay if we return normally, this is a weak test.
        } catch (SecurityException allowed) {
        }
    }

    private static boolean isInvokeAccessController() {
        for (StackTraceElement frame : new Throwable().getStackTrace()) {
            if ("invokeAccessController".equals(frame.getMethodName())
                    && LogManagerProperties.class.getName().equals(frame.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAccessController() {
        for (StackTraceElement frame : new Throwable().getStackTrace()) {
            if ("doPrivileged".equals(frame.getMethodName())
                    && "java.security.AccessController".equals(
                            frame.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testRun() {
        PrivilegedAction<Boolean> p = new PrivilegedAction<Boolean>() {
            @SuppressWarnings("override") //JDK-6954234
            public Boolean run() {
                assertFalse(isInvokeAccessController());
                assertFalse(isAccessController());
                return true;
            }
        };

        assertTrue(LogManagerProperties.runOrDoPrivileged(p));
    }

    @Test
    public void testDoPrivileged() {
        PrivilegedAction<Boolean> p = new PrivilegedAction<Boolean>() {
            @SuppressWarnings("override") //JDK-6954234
            public Boolean run() {
                if (isInvokeAccessController()) {
                    return isAccessController();
                }
                throw new SecurityException();
            }
        };

        try {
            assertTrue(LogManagerProperties.runOrDoPrivileged(p));
        } catch (UndeclaredThrowableException removed) {
            assertTrue(removed.getCause() instanceof ClassNotFoundException);
        } catch (UnsupportedOperationException terminal) {
        }
    }

    @Test
    public void testDoPrivilegedRuntimeException() {
        RuntimeException cause = new RuntimeException();
        PrivilegedAction<Boolean> p = new PrivilegedAction<Boolean>() {
            @SuppressWarnings("override") //JDK-6954234
            public Boolean run() {
                if (isInvokeAccessController()) {
                    throw cause;
                }
                throw new SecurityException();
            }
        };

        boolean ran = false;
        try {
            LogManagerProperties.runOrDoPrivileged(p);
            ran = true;
        } catch (UndeclaredThrowableException removed) {
            assertTrue(removed.getCause() instanceof ClassNotFoundException);
        } catch (UnsupportedOperationException terminal) {
        } catch (RuntimeException re) {
            assertSame(re, cause);
        }
        assertFalse(ran);
    }

    @Test
    public void testDoPrivilegedError() {
        Error cause = new Error();
        PrivilegedAction<Boolean> p = new PrivilegedAction<Boolean>() {
            @SuppressWarnings("override") //JDK-6954234
            public Boolean run() {
                if (isInvokeAccessController()) {
                    throw cause;
                }
                throw new SecurityException();
            }
        };

        boolean ran = false;
        try {
            LogManagerProperties.runOrDoPrivileged(p);
            ran = true;
        } catch (UndeclaredThrowableException removed) {
            assertTrue(removed.getCause() instanceof ClassNotFoundException);
        } catch (UnsupportedOperationException terminal) {
        } catch (Error e) {
            assertSame(e, cause);
        }
        assertFalse(ran);
    }

    @Test(expected=NullPointerException.class)
    public void testRunOrDoPrivilegedNull() {

        LogManagerProperties.runOrDoPrivileged((PrivilegedAction<?>) null);
    }

    @Test
    public void testFromLogManagerPresent() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            String value = "value";
            String emptyValue = "empty";
            Properties parent = new Properties();
            parent.put(key, value);
            parent.put("", emptyValue);

            read(manager, parent);
            assertTrue(LogManagerProperties.hasLogManager());
            assertEquals(value, LogManagerProperties.fromLogManager(key));
            assertEquals(emptyValue, LogManagerProperties.fromLogManager(""));
        } finally {
            manager.reset();
        }

        try {
            LogManagerProperties.fromLogManager((String) null);
            fail("");
        } catch (NullPointerException expect) {
        }
    }

    @Ignore
    public void testFromLogManagerNull() throws Exception {
        assumeNoJit();
        testFromLogManager((Properties) null);
    }

    @Ignore
    public void testFromLogManagerAbsent() throws Exception {
        assumeNoJit();
        final String cfgKey = "java.util.logging.config.file";
        final Class<?> k = LogManagerProperties.class;
        String old = System.getProperty(cfgKey);
        try {
            Properties props = new Properties();
            props.put("", "empty");
            props.put(k.getName().concat(".dummy"), "value");
            final File f = File.createTempFile(k.getName(), ".properties");
            try {
                try (FileOutputStream out = new FileOutputStream(f)) {
                    props.store(out, "testFromLogManagerAbsent");
                }
                System.setProperty(cfgKey, f.getAbsolutePath());
                final Method m = k.getDeclaredMethod("readConfiguration");
                assertTrue(Modifier.isPrivate(m.getModifiers()));
                m.setAccessible(true);
                props = (Properties) m.invoke(null);
                testFromLogManager(props);
            } finally {
                assertTrue(f.toString(), f.delete() || !f.exists());
            }
        } finally {
            if (old != null) {
                System.setProperty(cfgKey, old);
            } else {
                System.clearProperty(cfgKey);
            }
        }
    }

    private void testFromLogManager(Properties parent) throws Exception {
        assertTrue(LogManagerProperties.hasLogManager());
        final Class<?> k = LogManagerProperties.class;
        final Field f = k.getDeclaredField("LOG_MANAGER");
        Field mod = setAccessible(f);
        try {
            fullFence();
            final Object lm = f.get(null);
            f.set(null, parent);
            try {
                fullFence();
                assertFalse(LogManagerProperties.hasLogManager());
                if (parent != null) {
                    assertFalse(parent.isEmpty());
                    for (Map.Entry<Object, Object> e : parent.entrySet()) {
                        String key = e.getKey().toString();
                        String val = LogManagerProperties.fromLogManager(key);
                        assertEquals(e.getValue(), val);
                    }
                } else {
                    assertNull(LogManagerProperties.fromLogManager(""));
                    assertNull(LogManagerProperties.fromLogManager("val"));
                }

                try {
                    LogManagerProperties.fromLogManager((String) null);
                    fail("");
                } catch (NullPointerException expect) {
                }
            } finally {
                f.set(null, lm);
                fullFence();
            }
        } finally {
            mod.setInt(f, f.getModifiers() | Modifier.FINAL);
            fullFence();
        }
    }

    @Test
    public void testLinkage() throws Exception {
        testLinkage(LogManagerProperties.class);
    }

    @Test
    public void testWebappClassLoaderFieldNames() throws Exception {
        testWebappClassLoaderFieldNames(LogManagerProperties.class);
    }

    @Test
    public void testClone() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        Properties parent;
        LogManagerProperties mp;
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            parent = new Properties();
            parent.put(key, "value");

            read(manager, parent);

            parent = new Properties();
            mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertEquals("value", mp.getProperty(key));
            assertTrue(contains(mp, key, "value")); //ensure copy worked.
        } finally {
            manager.reset();
        }

        Properties clone = (Properties) mp.clone();
        assertFalse(clone instanceof LogManagerProperties);
        assertEquals(Properties.class, clone.getClass());
        assertNotSame(clone, parent);
        assertNotSame(clone, mp);
        assertEquals(mp.size(), clone.size());
        assertTrue(clone.equals(mp)); //don't call mp.equals.
    }

    @Test
    public void testIsReflection() throws Exception {
        assertTrue(LogManagerProperties.isReflectionClass(Constructor.class.getName()));
        assertTrue(LogManagerProperties.isReflectionClass(Method.class.getName()));
    }

    @Test
    public void testIsStaticUtilityClass() throws Exception {
        boolean nullCheck;
        try {
            LogManagerProperties.isStaticUtilityClass((String) null);
            nullCheck = false;
        } catch (NullPointerException expect) {
            nullCheck = true;
        }

        if (!nullCheck) {
            fail("Null check");
        }

        String[] utils = {
            "java.lang.System",
            "java.nio.channels.Channels",
            "java.util.Collections",
            "javax.mail.internet.MimeUtility",
            "org.junit.Assert"
        };

        testIsStaticUtilityClass(utils, true);

        String[] obj = {
            "java.lang.Exception",
            "java.lang.Object",
            "java.lang.Runtime",
            "java.io.Serializable"
        };
        testIsStaticUtilityClass(obj, false);

        String[] enumerations = {
            "java.util.concurrent.TimeUnit"
        };
        testIsStaticUtilityClass(enumerations, false);

        String[] fail = {
            "badClassName"
        };
        for (String name : fail) {
            boolean pass;
            try {
                LogManagerProperties.isStaticUtilityClass(name);
                pass = false;
            } catch (ClassNotFoundException expect) {
                pass = true;
            }

            if (!pass) {
                fail(name);
            }
        }
    }

    private void testIsStaticUtilityClass(String[] names, boolean complement) throws Exception {
        assertFalse(names.length == 0);

        if (complement) {
            for (String name : names) {
                assertTrue(name, LogManagerProperties.isStaticUtilityClass(name));
            }
        } else {
            for (String name : names) {
                assertFalse(name, LogManagerProperties.isStaticUtilityClass(name));
            }
        }
    }

    @Test
    public void testGetLocalHost() throws Exception {
        String host = LogManagerPropertiesTest.class.getName();
        Properties p = new Properties();
        p.setProperty("mail.smtp.localhost", host);
        Session s = Session.getInstance(p);
        Transport t = s.getTransport(InternetAddress.getLocalAddress(s));
        try {
            String h = LogManagerProperties.getLocalHost(t);
            if (h != null || isPrivateSpec(t.getClass())) {
                Assert.assertEquals(host, h);
            }
        } catch (NoSuchMethodException notOfficial) {
            if (isPrivateSpec(t.getClass())) {
                fail(t.toString());
            }
        }
    }

    @Test
    public void testGetLocalHostMissing() throws Exception {
        Session session = Session.getInstance(new Properties());
        Service svc = new NoHostService(session);
        try {
            LogManagerProperties.getLocalHost(svc);
            fail("");
        } catch (NoSuchMethodException expect) {
        }
    }

    @Test
    public void testGetLocalHostSecure() throws Exception {
        Session session = Session.getInstance(new Properties());
        Service svc = new NotAllowedService(session);
        try {
            LogManagerProperties.getLocalHost(svc);
        } catch (SecurityException allowed) {
        } catch (InvocationTargetException expect) {
            Assert.assertTrue(expect.getCause() instanceof SecurityException);
        }
    }

    @Test(expected=NoSuchMethodException.class)
    public void testGetLocalHostNoLoop() throws Exception {
        LogManagerProperties.getLocalHost(new Object());
    }

    @Test(expected=NullPointerException.class)
    public void testGetLocalHostNull() throws Exception {
        LogManagerProperties.getLocalHost((Service) null);
    }

    @Test
    public void testParseDurationMs() throws Exception {
        try {
            long ms = LogManagerProperties.parseDurationToMillis("PT0.345S");
            assertEquals(345L, ms);
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        }
    }

    @Test
    public void testParseDurationSec() throws Exception {
        try {
            long ms = LogManagerProperties.parseDurationToMillis("PT20.345S");
            assertEquals((20L * 1000L) + 345L, ms);
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        }
    }

    @Test
    public void testParseDurationMin() throws Exception {
        try {
            long ms = LogManagerProperties.parseDurationToMillis("PT15M");
            assertEquals(15L * 60L * 1000L, ms);
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        }
    }

    @Test
    public void testParseDurationHour() throws Exception {
        try {
            long ms = LogManagerProperties.parseDurationToMillis("PT10H");
            assertEquals(10L * 60L * 60L * 1000L, ms);
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        }
    }

    @Test
    public void testParseDurationDay() throws Exception {
        try {
            long ms = LogManagerProperties.parseDurationToMillis("P2D");
            assertEquals(2L * 24L * 60L * 60L * 1000L, ms);
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        }
    }

    @Test
    public void testParseDurationAll() throws Exception {
        try {
            long ms = LogManagerProperties
                    .parseDurationToMillis("P2DT3H4M20.345S");
            assertEquals((2L * 24L * 60L * 60L * 1000L)
                    + (3L * 60L * 60L * 1000L) + (4L * 60L * 1000L)
                    + ((20L * 1000L) + 345), ms);
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        }
    }

    @Test(expected=NullPointerException.class)
    public void testParseDurationNull() throws Exception {
        LogManagerProperties.parseDurationToMillis((CharSequence) null);
    }

    @Test
    public void testParseDurationParseException() throws Exception {
        try {
            String name = LogManagerPropertiesTest.class.getName();
            long ms = LogManagerProperties.parseDurationToMillis(name);
            fail(Long.toString(ms));
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        } catch (InvocationTargetException expected) {
            Throwable dtpe = expected.getCause();
            //Allow subclasses of DTPE
            Class<?> k = Class.forName(
                    "java.time.format.DateTimeParseException");
            assertTrue(k.getName(), k.isAssignableFrom(dtpe.getClass()));
        }
    }

    @Test
    public void testParseDurationParseOverflow() throws Exception {
        testParseDurationNotExact("PT2562047788015H12M55.808S");
    }

    @Test
    public void testParseDurationParseUnderflow() throws Exception {
        testParseDurationNotExact("PT-2562047788015H-12M-55.809S");
    }

    public void testParseDurationNotExact(String underOrOver) throws Exception {
        try {
            long ms = LogManagerProperties.parseDurationToMillis(underOrOver);
            fail(Long.toString(ms));
        } catch (ClassNotFoundException | NoClassDefFoundError ignore) {
            assertFalse(ignore.toString(), hasJavaTimeModule());
        } catch (ArithmeticException expected) {
        } catch (RuntimeException allowed) {
            //Allow subclasses of DTPE
            Class<?> k = Class.forName(
                    "java.time.format.DateTimeParseException");
            assertTrue(allowed.toString(),
                    k.isAssignableFrom(allowed.getClass()));
        }
    }

    @Test
    public void testGetProperty_String() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            Properties parent = new Properties();
            parent.put(key, "value");
            parent.put("", "empty");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertEquals("value", mp.getProperty(key));
            assertTrue(contains(mp, key, "value")); //ensure copy worked.
            parent.put(key, "newValue");
            assertEquals("newValue", mp.getProperty(key));
            assertEquals("empty", mp.getProperty(""));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testGetProperty_String_String() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            Properties parent = new Properties();
            parent.put(key, "value");
            parent.put("", "empty");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertEquals("value", mp.getProperty(key, null));
            assertTrue(contains(mp, key, "value")); //ensure copy worked.
            parent.put(key, "newValue");
            assertEquals("newValue", mp.getProperty(key, null));
            assertEquals("default", mp.getProperty("unknown", "default"));
            assertEquals("empty", mp.getProperty("", null));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testGet() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            Properties parent = new Properties();
            parent.put(key, "value");
            parent.put("", "empty");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertEquals("value", mp.get(key));
            assertTrue(contains(mp, key, "value")); //ensure copy worked.
            parent.put(key, "newValue");
            assertEquals("newValue", mp.get(key));
            assertEquals("empty", mp.get(""));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testGetObject() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        Properties parent = new Properties();
        LogManagerProperties mp = new LogManagerProperties(parent, prefix);
        String key = "key";
        Object value = new Object();
        parent.put(key, value);

        assertEquals(parent.get(key), mp.get(key));
    }

    @Test
    public void testContainsKey() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            Properties parent = new Properties();
            parent.put(key, "value");
            parent.put("", "empty");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertTrue(mp.containsKey(key));
            assertTrue(contains(mp, key, "value")); //ensure copy worked.
            parent.put(key, "newValue");
            assertEquals("newValue", mp.get(key));
            assertTrue(mp.containsKey(""));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testContainsKeyObject() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        Properties parent = new Properties();
        LogManagerProperties mp = new LogManagerProperties(parent, prefix);
        String key = "key";
        Object value = new Object();
        parent.put(key, value);
        assertEquals(parent.containsKey(key), mp.containsKey(key));
    }

    @Test
    public void testRemove() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            Properties parent = new Properties();
            parent.put(key, "value");
            parent.put("", "empty");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertEquals("value", mp.remove(key));
            assertFalse(contains(mp, key, "value")); //ensure copy worked.
            parent.put(key, "newValue");
            assertEquals("newValue", mp.remove(key));
            assertEquals("empty", mp.remove(""));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testRemoveObject() {
        String prefix = LogManagerPropertiesTest.class.getName();
        Properties parent = new Properties();
        LogManagerProperties mp = new LogManagerProperties(parent, prefix);
        String key = "key";
        Object value = new Object();
        parent.put(key, value);
        assertEquals(value, parent.remove(key));
        assertEquals(parent.containsKey(key), mp.containsKey(key));
        assertNull(parent.remove(key));
    }

    @Test
    public void testPut() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            Properties parent = new Properties();
            parent.put(key, "value");
            parent.put("", "empty");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertEquals("value", mp.put(key, "newValue"));
            assertFalse(contains(mp, key, "value")); //ensure copy worked.
            assertTrue(contains(mp, key, "newValue")); //ensure copy worked.
            parent.put(key, "defValue");
            assertEquals("newValue", mp.remove(key));
            assertEquals("defValue", mp.remove(key));
            assertEquals("empty", mp.put("", ""));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testPutObject() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        Properties parent = new Properties();
        LogManagerProperties mp = new LogManagerProperties(parent, prefix);
        String key = "key";
        Object value = TimeUnit.MILLISECONDS;
        assertNull(mp.put(key, value));
        Object newValue = TimeUnit.NANOSECONDS;
        assertEquals(value, mp.put(key, newValue));
        assertEquals(newValue, mp.get(key));
    }

    @Test
    public void testSetProperty() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String key = prefix.concat(".dummy");
            Properties parent = new Properties();
            parent.put(key, "value");
            parent.put("", "empty");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);

            assertFalse(contains(mp, key, null));
            assertEquals("value", mp.setProperty(key, "newValue"));
            assertFalse(contains(mp, key, "value")); //ensure copy worked.
            assertTrue(contains(mp, key, "newValue")); //ensure copy worked.
            parent.put(key, "defValue");
            assertEquals("newValue", mp.remove(key));
            assertEquals("defValue", mp.remove(key));
            assertEquals("empty", mp.setProperty("", ""));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testPropUtil() throws Exception {
        String prefix = LogManagerPropertiesTest.class.getName();
        LogManager manager = LogManager.getLogManager();
        try {
            String keyShort = "mail.smtp.reportsuccess";
            String key = prefix + '.' + keyShort;
            Properties parent = new Properties();
            parent.put(key, "true");

            read(manager, parent);

            parent = new Properties();
            LogManagerProperties mp = new LogManagerProperties(parent, prefix);
            assertFalse(contains(mp, keyShort, null));

            final Session session = Session.getInstance(mp);
            final Object t = session.getTransport("smtp");
            if (isPrivateSpec(t.getClass())) {
                final String clazzName = "com.sun.mail.smtp.SMTPTransport";
                assertEquals(clazzName, t.getClass().getName());
            } else {
                assertNotNull(t);
                session.getProperty(keyShort); //Force a read through session.
            }
            assertTrue(contains(mp, keyShort, "true"));
        } finally {
            manager.reset();
        }
    }

    @Test
    public void testToLanguageTag() throws Exception {
        assertEquals("en-US", LogManagerProperties.toLanguageTag(Locale.US));
        assertEquals("en", LogManagerProperties.toLanguageTag(Locale.ENGLISH));
        assertEquals("", LogManagerProperties.toLanguageTag(new Locale("", "", "")));
        Locale l = new Locale("en", "US", "slang");
        assertEquals("en-US-slang", LogManagerProperties.toLanguageTag(l));
        l = new Locale("en", "", "slang");
        assertEquals("en--slang", LogManagerProperties.toLanguageTag(l));

        try {
            LogManagerProperties.toLanguageTag(null);
            fail("Null was allowed.");
        } catch (NullPointerException expect) {
        }
    }

    @Test
    public void testNewObjectFrom() throws Exception {
        try {
            LogManagerProperties.newObjectFrom((String) null, Object.class);
            fail("Null name was allowed.");
        } catch (NullPointerException expect) {
        }

        try {
            LogManagerProperties.newObjectFrom(Object.class.getName(),
                    (Class<Object>) null);
            fail("Null class was allowed.");
        } catch (NullPointerException expect) {
        }

        try {
            LogManagerProperties.newObjectFrom((String) null,
                    (Class<Object>) null);
            fail("Null was allowed.");
        } catch (NullPointerException expect) {
        }

        try {
            LogManagerProperties.newObjectFrom("", Object.class);
            fail("Empty class was allowed.");
        } catch (ClassNotFoundException expect) {
        }

        try {
            LogManagerProperties.newObjectFrom(Object.class.getName(),
                    String.class);
            fail("Wrong type was allowed.");
        } catch (ClassCastException expect) {
        }

        Object o = LogManagerProperties.
                newObjectFrom(String.class.getName(), Object.class);
        assertEquals(String.class, o.getClass());

        String n = LogManagerProperties.
                newObjectFrom(String.class.getName(), String.class);
        assertEquals(String.class, n.getClass());
    }

    @Test
    public void testNewAuthenticator() throws Exception {
        Authenticator a = LogManagerProperties.newObjectFrom(
                EmptyAuthenticator.class.getName(),
                Authenticator.class);
        assertEquals(EmptyAuthenticator.class, a.getClass());

        final Class<?> type = ErrorAuthenticator.class;
        a = LogManagerProperties.newObjectFrom(
                type.getName(), Authenticator.class);
        assertEquals(type, a.getClass());

        setPending(new RuntimeException());
        try {
            LogManagerProperties.newObjectFrom(type.getName(),
                    Authenticator.class);
            fail("Exception was not thrown.");
        } catch (InvocationTargetException expect) {
            assertEquals(RuntimeException.class, expect.getCause().getClass());
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testNewComparator() throws Exception {
        try {
            LogManagerProperties.newComparator(null);
            fail("Null was allowed.");
        } catch (NullPointerException expect) {
        }

        try {
            LogManagerProperties.newComparator("");
            fail("Empty class was allowed.");
        } catch (ClassNotFoundException expect) {
        }

        try {
            LogManagerProperties.newComparator(Object.class.getName());
            fail("Wrong type was allowed.");
        } catch (ClassCastException expect) {
        }

        final Class<?> type = ErrorComparator.class;
        final Comparator<? super LogRecord> c
                = LogManagerProperties.newComparator(type.getName());
        assertEquals(type, c.getClass());

        setPending(new RuntimeException());
        try {
            LogManagerProperties.newComparator(type.getName());
            fail("Exception was not thrown.");
        } catch (InvocationTargetException expect) {
            assertEquals(RuntimeException.class, expect.getCause().getClass());
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testReverseOrder() throws Exception {
        try {
            LogManagerProperties.reverseOrder(null);
            fail("Null was allowed.");
        } catch (NullPointerException expect) {
        }

        Comparator<LogRecord> c = new ErrorComparator();
        Comparator<LogRecord> r = LogManagerProperties.reverseOrder(c);
        assertTrue(c.getClass() != r.getClass());
        assertFalse(r instanceof ErrorComparator);
        assertFalse(r instanceof AscComparator);
        assertFalse(r instanceof DescComparator);

        c = new AscComparator();
        r = LogManagerProperties.reverseOrder(c);
        assertTrue(r instanceof DescComparator);

        c = new AscComparator();
        r = LogManagerProperties.reverseOrder(c);
        assertTrue(r instanceof DescComparator);
    }

    @Test
    public void testNewErrorManager() throws Exception {
        try {
            LogManagerProperties.newErrorManager(null);
            fail("Null was allowed.");
        } catch (NullPointerException expect) {
        }

        try {
            LogManagerProperties.newErrorManager("");
            fail("Empty class was allowed.");
        } catch (ClassNotFoundException expect) {
        }

        try {
            LogManagerProperties.newErrorManager(Object.class.getName());
            fail("Wrong type was allowed.");
        } catch (ClassCastException expect) {
        }

        final Class<?> type = ErrorManager.class;
        ErrorManager f = LogManagerProperties.newErrorManager(type.getName());
        assertEquals(type, f.getClass());

        setPending(new RuntimeException());
        try {
            final String name = ErrorErrorManager.class.getName();
            LogManagerProperties.newErrorManager(name);
            fail("Exception was not thrown.");
        } catch (InvocationTargetException expect) {
            assertEquals(RuntimeException.class, expect.getCause().getClass());
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testNewFilter() throws Exception {
        try {
            LogManagerProperties.newFilter(null);
            fail("Null was allowed.");
        } catch (NullPointerException expect) {
        }

        try {
            LogManagerProperties.newFilter("");
            fail("Empty class was allowed.");
        } catch (ClassNotFoundException expect) {
        }

        try {
            LogManagerProperties.newFilter(Object.class.getName());
            fail("Wrong type was allowed.");
        } catch (ClassCastException expect) {
        }

        final Class<?> type = ErrorFilter.class;
        final Filter f = LogManagerProperties.newFilter(type.getName());
        assertEquals(type, f.getClass());

        setPending(new RuntimeException());
        try {
            LogManagerProperties.newFilter(type.getName());
            fail("Exception was not thrown.");
        } catch (InvocationTargetException expect) {
            assertEquals(RuntimeException.class, expect.getCause().getClass());
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testNewFormatter() throws Exception {
        try {
            LogManagerProperties.newFormatter(null);
            fail("Null was allowed.");
        } catch (NullPointerException expect) {
        }

        try {
            LogManagerProperties.newFormatter("");
            fail("Empty class was allowed.");
        } catch (ClassNotFoundException expect) {
        }

        try {
            LogManagerProperties.newFormatter(Object.class.getName());
            fail("Wrong type was allowed.");
        } catch (ClassCastException expect) {
        }

        final Class<?> type = SimpleFormatter.class;
        final Formatter f = LogManagerProperties.newFormatter(type.getName());
        assertEquals(type, f.getClass());

        setPending(new RuntimeException());
        try {
            final String name = ErrorFormatter.class.getName();
            LogManagerProperties.newFormatter(name);
            fail("Exception was not thrown.");
        } catch (InvocationTargetException expect) {
            assertEquals(RuntimeException.class, expect.getCause().getClass());
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testEscapingAuthenticator() throws Exception {
        try {
            Class<?> k = ErrorAuthenticator.class;
            javax.mail.Authenticator a;

            a = LogManagerProperties.newObjectFrom(k.getName(), Authenticator.class);
            assertEquals(k, a.getClass());

            setPending(new StackOverflowError());
            try {
                a = LogManagerProperties.newObjectFrom(k.getName(), Authenticator.class);
                fail(String.valueOf(a));
            } catch (StackOverflowError expect) {
            }

            setPending(new OutOfMemoryError());
            try {
                a = LogManagerProperties.newObjectFrom(k.getName(), Authenticator.class);
                fail(String.valueOf(a));
            } catch (OutOfMemoryError expect) {
            }
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testEscapingComparator() throws Exception {
        try {
            Class<?> k = ErrorComparator.class;
            Comparator<? super LogRecord> c;

            c = LogManagerProperties.newComparator(k.getName());
            assertEquals(k, c.getClass());

            setPending(new StackOverflowError());
            try {
                c = LogManagerProperties.newComparator(k.getName());
                fail(String.valueOf(c));
            } catch (StackOverflowError expect) {
            }

            setPending(new OutOfMemoryError());
            try {
                c = LogManagerProperties.newComparator(k.getName());
                fail(String.valueOf(c));
            } catch (OutOfMemoryError expect) {
            }
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testEscapingErrorErrorManager() throws Exception {
        try {
            Class<?> k = ErrorErrorManager.class;
            ErrorManager f;

            f = LogManagerProperties.newErrorManager(k.getName());
            assertEquals(k, f.getClass());

            setPending(new StackOverflowError());
            try {
                f = LogManagerProperties.newErrorManager(k.getName());
                fail(String.valueOf(f));
            } catch (StackOverflowError expect) {
            }

            setPending(new OutOfMemoryError());
            try {
                f = LogManagerProperties.newErrorManager(k.getName());
                fail(String.valueOf(f));
            } catch (OutOfMemoryError expect) {
            }
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testEscapingFilter() throws Exception {
        try {
            Class<?> k = ErrorFilter.class;
            Filter f;

            f = LogManagerProperties.newFilter(k.getName());
            assertEquals(k, f.getClass());

            setPending(new StackOverflowError());
            try {
                f = LogManagerProperties.newFilter(k.getName());
                fail(String.valueOf(f));
            } catch (StackOverflowError expect) {
            }

            setPending(new OutOfMemoryError());
            try {
                f = LogManagerProperties.newFilter(k.getName());
                fail(String.valueOf(f));
            } catch (OutOfMemoryError expect) {
            }
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testEscapingFormatter() throws Exception {
        try {
            Class<?> k = ErrorFormatter.class;
            Formatter f;

            f = LogManagerProperties.newFormatter(k.getName());
            assertEquals(k, f.getClass());

            setPending(new StackOverflowError());
            try {
                f = LogManagerProperties.newFormatter(k.getName());
                fail(String.valueOf(f));
            } catch (StackOverflowError expect) {
            }

            setPending(new OutOfMemoryError());
            try {
                f = LogManagerProperties.newFormatter(k.getName());
                fail(String.valueOf(f));
            } catch (OutOfMemoryError expect) {
            }
        } finally {
            setPending(null);
        }
    }

    @Test
    public void testGetLongThreadID() throws Exception {
        long id = Thread.currentThread().getId();
        if (id <= Integer.MAX_VALUE) {
           id = ((long) Integer.MAX_VALUE) + 1L;
        } else {
           id++;
        }

        LogRecord r1 = new LogRecord(Level.SEVERE, "");
        LogRecord r2 = new LogRecord(Level.SEVERE, "");
        try {
            setLongThreadID(r1, id);
            setLongThreadID(r2, id);
            assertEquals(id, (long) LogManagerProperties.getLongThreadID(r1));
            assertEquals(id, (long) LogManagerProperties.getLongThreadID(r2));
        } catch (final NoSuchMethodException preJdkSixteen) {
            try {
              Method m = LogRecord.class.getMethod("getLongThreadID");
              fail(m.toString());
            } catch (NoSuchMethodException expect) {
                assertNull(LogManagerProperties.getLongThreadID(r1));
            }
        }
    }

    @Test(expected=NullPointerException.class)
    public void testGetLongThreadIDNull() throws Exception {
        LogManagerProperties.getLongThreadID((LogRecord) null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetZonedDateTime() throws Exception {
        LogRecord r1 = new LogRecord(Level.SEVERE, "");
        LogRecord r2 = new LogRecord(Level.SEVERE, "");
        try {
            final Class<?> k = Class.forName("java.time.ZonedDateTime");
            setEpochSecond(r1, 100, 1);
            setEpochSecond(r2, 100, 1);
            Comparable<Object> c1 = (Comparable<Object>)
                LogManagerProperties.getZonedDateTime(r1);
            Comparable<Object> c2 = (Comparable<Object>)
                LogManagerProperties.getZonedDateTime(r2);

            assertEquals(k, c1.getClass());
            assertEquals(k, c2.getClass());
            assertNotSame(c1, c2);
            assertEquals(c1.getClass(), c2.getClass());
            assertEquals(0, c1.compareTo(c2));
        } catch (final NoSuchMethodException preJdk9) {
            assertNull(LogManagerProperties.getZonedDateTime(r1));
            assertNull(LogManagerProperties.getZonedDateTime(r2));
            assertTrue(hasJavaTimeModule());
        } catch (final ClassNotFoundException preJdk8) {
            assertNull(LogManagerProperties.getZonedDateTime(r1));
            assertNull(LogManagerProperties.getZonedDateTime(r2));
            assertFalse(hasJavaTimeModule());
        }
    }

    @Test(expected=NullPointerException.class)
    public void testGetZonedDateTimeNull() throws Exception {
        LogManagerProperties.getZonedDateTime((LogRecord) null);
    }

    private static void setPending(final Throwable t) {
        if (t != null) {
            PENDING.set(t);
        } else {
            PENDING.remove();
        }
    }

    static void throwPendingIfSet() {
        Throwable t = PENDING.get();
        if (t != null) {
            if (t instanceof Error) {
                t = t.fillInStackTrace();
                assert t instanceof Error : t;
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                t = t.fillInStackTrace();
                assert t instanceof RuntimeException : t;
                throw (RuntimeException) t;
            } else {
                throw new AssertionError(t);
            }
        }
    }

    private static Field setAccessible(Field f) {
        f.setAccessible(true);
        try {
            assumeNoJit();
            if (Modifier.isFinal(f.getModifiers())) {
                Field mod = Field.class.getDeclaredField("modifiers");
                mod.setAccessible(true);
                mod.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                return mod;
            }
        } catch (RuntimeException | ReflectiveOperationException re) {
            Assume.assumeNoException(re);
        }
        throw new AssertionError();
    }

    private boolean contains(Properties props, String key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        //walk the entry set so we don't preload a key from the manager.
        for (Map.Entry<?, ?> e : props.entrySet()) {
            if (key.equals(e.getKey())) {
                return value.equals(e.getValue());
            }
        }
        return false;
    }

    public static final class EmptyAuthenticator extends javax.mail.Authenticator {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return null;
        }
    }

    public static final class ErrorAuthenticator extends javax.mail.Authenticator {

        public ErrorAuthenticator() {
            throwPendingIfSet();
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            throw new Error("");
        }
    }

    public static class ErrorComparator implements Comparator<LogRecord>,
            Serializable {

        private static final long serialVersionUID = 1L;

        public ErrorComparator() {
            throwPendingIfSet();
        }

        @SuppressWarnings("override") //JDK-6954234
        public int compare(LogRecord r1, LogRecord r2) {
            throw new Error("");
        }
    }

    public static class AscComparator implements Comparator<LogRecord>,
            Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("override") //JDK-6954234
        public int compare(LogRecord r1, LogRecord r2) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("override")
        public Comparator<LogRecord> reversed() {
            return new DescComparator();
        }
    }

    public static class DescComparator implements Comparator<LogRecord>,
            Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("override") //JDK-6954234
        public int compare(LogRecord r1, LogRecord r2) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("override")
        public Comparator<LogRecord> reversed() {
            return new AscComparator();
        }
    }

    public static class ErrorFilter implements Filter {

        public ErrorFilter() {
            throwPendingIfSet();
        }

        @SuppressWarnings("override") //JDK-6954234
        public boolean isLoggable(LogRecord record) {
            throw new Error("");
        }
    }

    public static class ErrorFormatter extends Formatter {

        public ErrorFormatter() {
            throwPendingIfSet();
        }

        @Override
        public String format(LogRecord record) {
            throw new Error("");
        }
    }

    public static class ErrorErrorManager extends ErrorManager {

        public ErrorErrorManager() {
            throwPendingIfSet();
        }

        @Override
        public void error(String msg, Exception ex, int code) {
            throw new Error("");
        }
    }

    private static final class NoHostService extends Service {

        public NoHostService(Session session) {
            super(session, new URLName("test://somehost"));
        }
    }

    private static final class NotAllowedService extends Service {

        public NotAllowedService(Session session) {
            super(session, new URLName("test://somehost"));
        }

        public String getLocalHost() {
            throw new SecurityException();
        }
    }
}
