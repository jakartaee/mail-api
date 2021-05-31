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

package com.sun.mail.util;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.mail.util.StreamProvider;

/**
 * Contains the required encoders/decoders and streams required by the API.
 *
 */
public class MailStreamProvider implements StreamProvider {

	@Override
	public InputStream inputBase64(InputStream in) {
		return new BASE64DecoderStream(in);
	}

	@Override
	public OutputStream outputBase64(OutputStream out) {
		return new BASE64EncoderStream(out);
	}

	@Override
	public InputStream inputBinary(InputStream in) {
		return in;
	}

	@Override
	public OutputStream outputBinary(OutputStream out) {
		return out;
	}

	@Override
	public OutputStream outputB(OutputStream out) {
		return new BEncoderStream(out);
	}

	@Override
	public InputStream inputQ(InputStream in) {
		return new QDecoderStream(in);
	}

	@Override
	public OutputStream outputQ(OutputStream out, boolean encodingWord) {
		return new QEncoderStream(out, encodingWord);
	}

	@Override
	public LineInputStream inputLineStream(InputStream in, boolean allowutf8) {
		return new LineInputStream(in, allowutf8);
	}

	@Override
	public LineOutputStream outputLineStream(OutputStream out, boolean allowutf8) {
		return new LineOutputStream(out, allowutf8);
	}

	@Override
	public InputStream inputQP(InputStream in) {
		return new QPDecoderStream(in);
	}

	@Override
	public OutputStream outputQP(OutputStream out) {
		return new QPEncoderStream(out);
	}

	@Override
	public InputStream inputSharedByteArray(byte[] bytes) {
		return new SharedByteArrayInputStream(bytes);
	}

	@Override
	public InputStream inputUU(InputStream in) {
		return new UUDecoderStream(in);
	}

	@Override
	public OutputStream outputUU(OutputStream out, String filename) {
		if (filename == null) {
			return new UUEncoderStream(out);
		} else {
			return new UUEncoderStream(out, filename);
		}
	}

}
