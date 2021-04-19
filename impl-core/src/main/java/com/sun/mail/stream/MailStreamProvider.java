package com.sun.mail.stream;

import java.io.InputStream;
import java.io.OutputStream;

import jakarta.mail.stream.StreamProvider;

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
	public InputStream inputB(InputStream in) {
		throw new UnsupportedOperationException("Not supported");
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
	public OutputStream outputSharedByteArray(int size) {
		return new SharedByteArrayOutputStream(size);
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
