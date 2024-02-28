/*
 * Copyright (c) 2021, 2023 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class FactoryFinderTest {

    public FactoryFinderTest() {
    }

    @Test
    public void specifiedInSystemProperty() {
        System.setProperty(Class1.class.getName(), Class2.class.getName());
        Class1 impl = FactoryFinder.find(Class1.class);
        assertEquals(Class2.class, impl.getClass());
    }

    @Test
    public void specifiedInServiceLoader() {
        StreamProvider impl = FactoryFinder.find(StreamProvider.class);
        assertEquals(DummyStreamProvider.class, impl.getClass());
    }

    @Test
    public void doesNotExist() {
        try {
            FactoryFinder.find(Class2.class);
            fail("IllegalStateException is expected");
        } catch (IllegalStateException e) {
            assertNull(e.getCause());
        }
//        fails on SE 21-b16
//        try {
//            FactoryFinder.find(Class3.class);
//            fail("IllegalStateException is expected");
//        } catch (IllegalStateException e) {
//            // java.util.ServiceConfigurationError: jakarta.mail.util.FactoryFinderTest$Class3: module jakarta.mail does not declare `uses`
//            assertEquals(ServiceConfigurationError.class, e.getCause().getClass());
//        }
    }


    @Test
    public void contextClassLoaderIsBootLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader((ClassLoader) null);
        //Thread.currentThread().setContextClassLoader(ClassLoader.getPlatformClassLoader());
        try {
            doesNotExist();
            specifiedInSystemProperty();
            specifiedInServiceLoader();
        } finally {
            Thread.currentThread().setContextClassLoader(cl);
        }
    }

    public static class Class1 {
        public Class1() {}
    }

    public static class Class2 extends Class1 {
        public Class2() {}
    }

    public static class Class3 {
        public Class3() {}
    }
}
