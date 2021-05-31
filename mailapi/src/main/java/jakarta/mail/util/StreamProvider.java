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

package jakarta.mail.util;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface to be implemented by different mail implementations. It contains
 * the methods to instance different encoders/decoders and other streams required by the API.
 * 
 * Implementations of this interface will need to be declared in
 * META-INF/services/jakarta.mail.stream.StreamProvider
 *
 */
public interface StreamProvider {

	public static enum EncoderTypes {
		
		BASE_64("base64"),
		B_ENCODER("b"),
		Q_ENCODER("q"),
		BINARY_ENCODER("binary"),
		BIT7_ENCODER("7bit"),
		BIT8_ENCODER("8bit"),
		QUOTED_PRINTABLE_ENCODER("quoted-printable"),
		UU_ENCODER("uuencode"),
		X_UU_ENCODER("x-uuencode"),
		X_UUE("x-uue");
		
		private final String encoder;
		
		private EncoderTypes(String encoder) {
			this.encoder = encoder;
		}

		public String getEncoder() {
			return encoder;
		}
	}

    /**
     * Creates a 'base64' decoder from the InputStream.
     * @param in the InputStream
     * @return the decoder
     */
    InputStream inputBase64(InputStream in);

    /**
     * Creates a 'base64' encoder from the OutputStream.
     * @param out the OutputStream
     * @return the encoder
     */
    OutputStream outputBase64(OutputStream out);

    /**
     * Creates a 'binary', '7bit' and '8bit' decoder from the InputStream.
     * @param in the InputStream
     * @return the decoder
     */
    InputStream inputBinary(InputStream in);

    /**
     * Creates a 'binary', '7bit' and '8bit' encoder from the OutputStream.
     * @param out the OutputStream
     * @return the encoder
     */
    OutputStream outputBinary(OutputStream out);

    /**
     * Creates a 'b' encoder from the OutputStream.
     * @param out the OutputStream
     * @return the encoder
     */
	OutputStream outputB(OutputStream out);

    /**
     * Creates a 'q' decoder from the InputStream.
     * @param in the InputStream
     * @return the decoder
     */
	InputStream inputQ(InputStream in);

	/**
	 * Creates a 'q' encoder.
	 * @param out the OutputStream
	 * @param encodingWord true if we are Q-encoding a word within a phrase.
	 * @return the encoder
	 */
	OutputStream outputQ(OutputStream out, boolean encodingWord);

	/**
	 * Creates a new LineInputStream that supports reading CRLF terminated lines
	 * containing only US-ASCII characters from an input stream
	 * @param in the InputStream
	 * @param allowutf8	allow UTF-8 characters?
	 * @return the LineInputStream
	 */
	LineInputStream inputLineStream(InputStream in, boolean allowutf8);

	/**
	 * Creates a new LineOutputStream that supports writing out Strings as a sequence of bytes terminated
	 * by a CRLF sequence. The String must contain only US-ASCII characters.
	 * @param out the OutputStream
	 * @param allowutf8	allow UTF-8 characters?
	 * @return the LineOutputStream
	 */
	LineOutputStream outputLineStream(OutputStream out, boolean allowutf8);

    /**
     * Creates a 'quoted-printable' decoder from the InputStream.
     * @param in the InputStream
     * @return the decoder
     */
	InputStream inputQP(InputStream in);

    /**
     * Creates a 'quoted-printable' encoder from the OutputStream.
     * @param out the OutputStream
     * @return the encoder
     */
	OutputStream outputQP(OutputStream out);

	/**
	 * Creates a new InputStream from the underlying byte array to be shared
	 * between multiple readers.
	 * @param buf the byte array
	 * @return the InputStream
	 */
	InputStream inputSharedByteArray(byte[] buff);

    /**
     * Creates a 'uuencode', 'x-uuencode' and 'x-uue' decoder from the InputStream.
     * @param in the InputStream
     * @return the decoder
     */
	InputStream inputUU(InputStream in);

	/**
	 *  Creates a 'uuencode', 'x-uuencode' and 'x-uue' encoder from the OutputStream.
	 * @param out the OutputStream
	 * @param filename Specifies a name for the encoded buffer. It can be null.
	 * @return the encoder
	 */
	OutputStream outputUU(OutputStream out, String filename);
}
