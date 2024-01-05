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
import java.util.Arrays;
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
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            result = find(factoryClass, loader);
            if (result != null) {
                return result;
            }
        }

        //JakartaMail API ClassLoader / caller classloader
        loader = factoryClass.getClassLoader();
        if (loader != null) {
            result = find(factoryClass, loader);
            if (result != null) {
                return result;
            }
        }

        //Fallback to system class loader
        result = find(factoryClass, ClassLoader.getSystemClassLoader());
        if (result != null) {
            return result;
        }

        throw new IllegalStateException("No provider of " + factoryClass.getName() + " was found");
    }

    private static <T> T find(Class<T> factoryClass, ClassLoader loader) throws RuntimeException {
        // Use the system property first
        String className = fromSystemProperty(factoryClass.getName());
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

        T result = lookupUsingHk2ServiceLoader(factoryClass, loader);
        if (result != null) {
            return result;
        }

        return null;
    }

    private static <T> T newInstance(String className, Class<T> factoryClass, ClassLoader classLoader) throws RuntimeException {
        checkPackageAccess(className);
        try {
            Class<?> clazz;
            if (classLoader == null) { //Match behavior of ServiceLoader
                classLoader = ClassLoader.getSystemClassLoader();
            }
            clazz = Class.forName(className, false, classLoader);
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
    
    private static Class<?>[] getHk2ServiceLoaderTargets(Class<?> factoryClass) {
        ClassLoader[] loaders = new ClassLoader[]{
                Thread.currentThread().getContextClassLoader(), 
                    factoryClass.getClassLoader(), 
                    ClassLoader.getSystemClassLoader()};
        
        Class<?>[] classes = new Class<?>[loaders.length];
        int w = 0;
        for (ClassLoader loader : loaders) {
            if (loader != null) {
                try {
                    classes[w++] = Class.forName("org.glassfish.hk2.osgiresourcelocator.ServiceLoader", false, loader);
                } catch (Exception | LinkageError ignored) {  
                } //GlassFish class loaders can throw undocumented exceptions
            }
        }
        
        if (classes.length != w) {
           classes = Arrays.copyOf(classes, w);
        }
        return classes;
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T lookupUsingHk2ServiceLoader(Class<T> factoryClass, ClassLoader loader) {
        for (Class<?> target : getHk2ServiceLoaderTargets(factoryClass)) {
            try {
                // Use reflection to avoid having any dependency on HK2 ServiceLoader class
                Class<?> serviceClass = Class.forName(factoryClass.getName(), false, loader);
                Class<?>[] args = new Class<?>[]{serviceClass};
                Method m = target.getMethod("lookupProviderInstances", Class.class);
                Iterable<?> iterable = ((Iterable<?>) m.invoke(null, (Object[]) args));
                if (iterable != null) {
                    Iterator<?> iter = iterable.iterator();
                    if (iter.hasNext()) {
                        return factoryClass.cast(iter.next()); //Verify classloader.
                    }
                }
            } catch (Exception ignored) {
                // log and continue
            }
        }
        return null;
    }

    private static <T> T factoryFromServiceLoader(Class<T> factory, ClassLoader loader) {
        //ClassLoader of null is treated as system classloader
        try {
            ServiceLoader<T> sl = ServiceLoader.load(factory, loader);
            Iterator<T> iter = sl.iterator();
            if (iter.hasNext()) {
                return factory.cast(iter.next()); //Verify loader
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

