package com.echovantage.arrest.mime;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collection;

import org.echovantage.wonton.Wonton;

public abstract class AbstractMimeType implements MimeType {
	public class Decorator extends AbstractMimeType {
		private final AbstractMimeType type;
		private final Charset charset;

		public Decorator(final AbstractMimeType type, final Charset charset) {
			this.type = type;
			this.charset = charset;
		}

		@Override
		public Charset charset() {
			return charset;
		}

		@Override
		protected String contentType(final Charset cs) {
			return type.contentType(cs);
		}

		@Override
		public String mimeType() {
			return type.mimeType();
		}

		@Override
		public Collection<Charset> charsets() {
			return type.charsets();
		}

		@Override
		public MimeType withCharset(final Charset cs) {
			return type.withCharset(cs);
		}

		@Override
		public Wonton read(final ByteBuffer input) throws IOException, ParseException {
			return type.read(input);
		}

		@Override
		public ByteBuffer write(final Wonton result) throws IOException {
			return type.write(result);
		}
	}

	@Override
	public MimeType withCharset(final Charset charset) {
		if(charset().equals(charset)) {
			return this;
		}
		if(charsets().contains(charset)) {
			return new Decorator(this, charset);
		}
		return this;
	}

	@Override
	public final String contentType() {
		return contentType(charset());
	}

	protected String contentType(final Charset charset) {
		return mimeType();
	}
}
