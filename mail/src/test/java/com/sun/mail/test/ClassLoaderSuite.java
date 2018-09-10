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

package com.sun.mail.test;

import java.lang.annotation.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ArrayList;

import org.junit.runners.Suite;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

/**
 * A special test suite that loads each of the test classes
 * in a separate class loader, along with the class under test.
 * This allows the tests to test methods whose behavior depends on
 * the value of a System property that's read at class initialization
 * time; each test can set a different value of the System property
 * and the corresponding class under test will be loaded in a
 * separate class loader. <p>
 *
 * To use this class, create a test suite class:
 *
 * <pre>
 * @RunWith(ClassLoaderSuite.class)
 * @SuiteClasses({ MyTest1.class, MyTest2.class })
 * @TestClass(ClassToTest.class)
 * public class MyTestSuite {
 * }
 * </pre>
 *
 * The MyTest1 and MyTest2 classes are written as normal JUnit
 * test classes.  Set the System property to test in the @BeforeClass
 * method of these classes.
 *
 * @author Bill Shannon
 */

public class ClassLoaderSuite extends Suite {
    /**
     * An annotation to be used on the test suite class to indicate
     * the class under test.  The class is used to find the classpath
     * to allow loading the class under test in a separate class loader.
     * Note that other classes in the same classpath will also be loaded
     * in the separate class loader, along with the test classes.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface TestClass {
	public Class<?> value();
    }

    /**
     * A special class loader that loads classes from its own class path
     * (specified via URLs) before delegating to the parent class loader.
     * This is used to load the test classes in separate class loaders,
     * even though those classes are also loaded in the parent class loader.
     */
    static class TestClassLoader extends URLClassLoader {
	public TestClassLoader(URL[] urls, ClassLoader parent) {
	    super(urls, parent);
	}

	@Override
	public Class<?> loadClass(String name, boolean resolve)
				    throws ClassNotFoundException {
	    Class<?> c = null;
	    try {
		c = findLoadedClass(name);
		if (c != null)
		    return c;
		c = findClass(name);
		if (resolve)
		    resolveClass(c);
	    } catch (ClassNotFoundException cex) {
		c = super.loadClass(name, resolve);
	    }
	    return c;
	}
    }

    /**
     * Constructor.
     */
    public ClassLoaderSuite(Class<?> klass, RunnerBuilder builder)
				throws InitializationError {
	super(builder, klass,
	    reloadClasses(getTestClass(klass), getSuiteClasses(klass)));
    }

    /**
     * Set the thread's context class loader to the class loader
     * for the test class.
     */
    @Override
    protected void runChild(Runner runner, RunNotifier notifier) {
	// XXX - is it safe to assume it's always a ParentRunner?
	ParentRunner<?> pr = (ParentRunner<?>)runner;
	ClassLoader cl = null;
	try {
	    cl = Thread.currentThread().getContextClassLoader();
	    Thread.currentThread().setContextClassLoader(
		pr.getTestClass().getJavaClass().getClassLoader());
	    super.runChild(runner, notifier);
	} finally {
	    Thread.currentThread().setContextClassLoader(cl);
	}
    }

    /**
     * Get the value of the SuiteClasses annotation.
     */
    private static Class<?>[] getSuiteClasses(Class<?> klass)
				throws InitializationError {
	SuiteClasses annotation = klass.getAnnotation(SuiteClasses.class);
	if (annotation == null)
	    throw new InitializationError("class '" + klass.getName() +
		"' must have a SuiteClasses annotation");
	return annotation.value();
    }

    /**
     * Get the value of the TestClass annotation.
     */
    private static Class<?> getTestClass(Class<?> klass)
				throws InitializationError {
	TestClass annotation = klass.getAnnotation(TestClass.class);
	if (annotation == null)
	    throw new InitializationError("class '" + klass.getName() +
		"' must have a TestClass annotation");
	return annotation.value();
    }

    /**
     * Reload the classes in a separate class loader.
     */
    private static Class<?>[] reloadClasses(Class<?> testClass,
			Class<?>[] suiteClasses) throws InitializationError {
	URL[] urls = new URL[] {
	    classpathOf(testClass),
	    classpathOf(ClassLoaderSuite.class)
	};
	Class<?> sc = null;
	try {
	    for (int i = 0; i < suiteClasses.length; i++) {
		sc = suiteClasses[i];
		ClassLoader cl = new TestClassLoader(urls,
		    ClassLoaderSuite.class.getClassLoader());
		suiteClasses[i] = cl.loadClass(sc.getName());
	    }
	    return suiteClasses;
	} catch (ClassNotFoundException cex) {
	    throw new InitializationError("could not reload class: " + sc);
	}
    }

    /**
     * Return the classpath entry used to load the named resource.
     * XXX - Only handles file: and jar: URLs.
     */
    private static URL classpathOf(Class<?> c) {
	String name = "/" + c.getName().replace('.', '/') + ".class";
	try {
	    URL url = ClassLoaderSuite.class.getResource(name);
	    if (url.getProtocol().equals("file")) {
		String file = url.getPath();
		if (file.endsWith(name))	// has to be true?
		    file = file.substring(0, file.length() - name.length() + 1);
//System.out.println("file URL " + url + " has CLASSPATH " + file);
		return new URL("file", null, file);
	    } else if (url.getProtocol().equals("jar")) {
		String file = url.getPath();
		int i = file.lastIndexOf('!');
		if (i >= 0)
		    file = file.substring(0, i);
//System.out.println("jar URL " + url + " has CLASSPATH " + file);
		return new URL(file);
	    } else
		return url;
	} catch (MalformedURLException mex) {
	    return null;
	}
    }
}
