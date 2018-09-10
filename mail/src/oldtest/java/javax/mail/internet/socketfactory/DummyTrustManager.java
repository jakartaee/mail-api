/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;


/**
 * DummyTrustManager
 */
public class DummyTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] cert, String authType) {
	// everything is trusted
    }

    public void checkServerTrusted(X509Certificate[] cert, String authType) {
	// everything is trusted
    }

    public X509Certificate[] getAcceptedIssuers() {
	return new X509Certificate[0];
    }
}
