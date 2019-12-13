/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;


/**
 * DummySocketFactory
 */
public class DummySocketFactory extends SocketFactory {
    private SocketFactory factory;

    public DummySocketFactory() {
	factory = SocketFactory.getDefault();
    }

    public static SocketFactory getDefault() {
	return new DummySocketFactory();
    }

    public Socket createSocket() throws IOException {
	TestResult.success();
	return factory.createSocket();
    }

    public Socket createSocket(InetAddress inaddr, int i,
				InetAddress inaddr1, int j) throws IOException {
	TestResult.success();
	return factory.createSocket(inaddr, i, inaddr1, j);
    }

    public Socket createSocket(InetAddress inaddr, int i)
				throws IOException {
	TestResult.success();
	return factory.createSocket(inaddr, i);
    }

    public Socket createSocket(String s, int i, InetAddress inaddr, int j)
				throws IOException {
	TestResult.success();
	return factory.createSocket(s, i, inaddr, j);
    }

    public Socket createSocket(String s, int i) throws IOException {
	TestResult.success();
	return factory.createSocket(s, i);
    }
}
