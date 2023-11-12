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

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;

class FactoryFinder {

    /**
     * Finds the implementation {@code Class} object for the given
     * factory type.
     * The arguments supplied must be used in order
     * This method is package private so that this code can be shared.
     *
     * @param factoryClass factory abstract class or interface to be found
     * @return the {@code Class} object of the specified message factory
     * @throws RuntimeException if there is an error
     */
    static <T> T find(Class<T> factoryClass) throws RuntimeException {
        T result;
        result = find(factoryClass, Thread.currentThread().getContextClassLoader());
        if (result != null) {
            return result;
        }

        result = find(factoryClass, factoryClass.getClassLoader());
        if (result != null) {
            return result;
        }

        result = find(factoryClass, FactoryFinder.class.getClassLoader());
        if (result != null) {
            return result;
        }

        result = find(factoryClass, ClassLoader.getSystemClassLoader());
        if (result != null) {
            return result;
        }

        throw new IllegalStateException("No provider of " + factoryClass.getName() + " was found");
    }

    private static <T> T find(Class<T> factoryClass, ClassLoader loader) throws RuntimeException {
        String factoryId = factoryClass.getName();

        // Use the system property first
        String className = fromSystemProperty(factoryId);
        if (className != null) {
            T result = newInstance(className, factoryClass, loader);
            if (result != null) {
                return result;
            }
        }

        // standard services: java.util.ServiceLoader
        T factory = factoryFromServiceLoader(factoryClass, loader);
        if (factory != null) {
            return factory;
        }

        // handling Glassfish/OSGi (platform specific default)

        T result = lookupUsingHk2ServiceLoader(factoryId, factoryClass, loader);
        if (result != null) {
            return result;
        }

        return null;
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T newInstance(String className, Class<T> factoryClass, ClassLoader classLoader) throws RuntimeException {
        checkPackageAccess(className);
        try {
            Class<T> clazz;
            if (classLoader == null) {
                clazz = (Class<T>) Class.forName(className);
            } else {
                clazz = (Class<T>) Class.forName(className, false, classLoader);
            }
            return clazz.asSubclass(factoryClass).getConstructor().newInstance();
        } catch (ClassCastException wrongLoader) {
            return null;
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot instance " + className, e);
        }
    }

    private static String fromSystemProperty(String factoryId) {
        String systemProp = System.getProperty(factoryId);
        return systemProp;
    }

    private static final String OSGI_SERVICE_LOADER_CLASS_NAME = "org.glassfish.hk2.osgiresourcelocator.ServiceLoader";

    @SuppressWarnings({"unchecked"})
    private static <T> T lookupUsingHk2ServiceLoader(String factoryId, Class<T> factoryClass, ClassLoader loader) {
        Class<?> target;
        try {
            target = Class.forName(OSGI_SERVICE_LOADER_CLASS_NAME, false, loader);
        } catch (ClassNotFoundException ignored) {
            return null;
        }

        try {
            // Use reflection to avoid having any dependency on HK2 ServiceLoader class
            Class<?> serviceClass = Class.forName(factoryId, false, loader);
            Class<?>[] args = new Class<?>[]{serviceClass};
            Method m = target.getMethod("lookupProviderInstances", Class.class);
            Iterable<?> iterable = ((Iterable<?>) m.invoke(null, (Object[]) args));
            if (iterable == null) {
                return null;
            }
            Iterator<?> iter = iterable.iterator();
            return iter.hasNext() ? factoryClass.cast(iter.next()) : null;
        } catch (Exception ignored) {
            // log and continue
            return null;
        }
    }

    private static <T> T factoryFromServiceLoader(Class<T> factory, ClassLoader loader) {
        try {
            ServiceLoader<T> sl = ServiceLoader.load(factory, loader);
            Iterator<T> iter = sl.iterator();
            if (iter.hasNext()) {
                return factory.cast(iter.next());
            } else {
                return null;
            }
        }  catch (ClassCastException wrongLoader) {
            return null;
        } catch (Throwable t) {
            // For example, ServiceConfigurationError can be thrown if the factory class is not declared with 'uses' in module-info
            throw new IllegalStateException("Cannot load " + factory + " as ServiceLoader", t);
        }
    }

    private static void checkPackageAccess(String className) {
        // make sure that the current thread has an access to the package of the given name.
        SecurityManager s = System.getSecurityManager();
        if (s != null) {
            int i = className.lastIndexOf('.');
            if (i != -1) {
                s.checkPackageAccess(className.substring(0, i));
            }
        }
    }
}

