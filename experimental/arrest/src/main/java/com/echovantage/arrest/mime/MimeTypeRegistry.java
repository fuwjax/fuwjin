package com.echovantage.arrest.mime;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.echovantage.wonton.Wonton;

public class MimeTypeRegistry {
	public static final MimeTypeRegistry MIMETYPES = new MimeTypeRegistry();
	private final MimeType ANY = new AbstractMimeType() {
		private MimeType resolved;

		@Override
		public Charset charset() {
			return resolved == null ? null : resolved.charset();
		}

		@Override
		protected String contentType(final Charset charset) {
			return resolved == null ? null : resolved.contentType();
		}

		@Override
		public String mimeType() {
			return resolved == null ? null : resolved.mimeType();
		}

		@Override
		public Collection<Charset> charsets() {
			return resolved == null ? Collections.emptySet() : resolved.charsets();
		}

		@Override
		public MimeType withCharset(final Charset charset) {
			return resolved == null ? null : resolved.withCharset(charset);
		}

		@Override
		public Wonton read(final ByteBuffer input) throws IOException, ParseException {
			if(resolved != null) {
				return resolved.read(input);
			}
			int count = 0;
			for(MimeType type : types()) {
				count++;
				try {
					input.rewind();
					return type.read(input);
				} catch(IOException | ParseException e) {
					if(count == mimeTypes.size()) {
						throw e;
					}
				}
			}
			throw new IllegalStateException("No registered mime types");
		}

		@Override
		public ByteBuffer write(final Wonton result) throws IOException {
			if(resolved == null) {
				throw new IllegalStateException("Cannot write with an unresolved mime type");
			}
			return resolved.write(result);
		}
	};
	private static final Pattern CHARSET = Pattern.compile("\\s*;\\s*charset\\s*=\\s*([a-zA-Z0-9._-]+)");
	private final Map<String, MimeType> mimeTypes = new HashMap<>();

	private MimeTypeRegistry() {
		final ServiceLoader<MimeType> services = ServiceLoader.load(MimeType.class);
		for(final MimeType mimeType : services) {
			mimeTypes.put(mimeType.mimeType(), mimeType);
		}
	}

	public Collection<String> keys() {
		return mimeTypes.keySet();
	}

	public MimeType get(final String contentType) {
		if(contentType == null) {
			return ANY;
		}
		final Matcher matcher = CHARSET.matcher(contentType);
		MimeType mimeType = null;
		if(matcher.find()) {
			try {
				Charset charset = Charset.forName(matcher.group(1));
				mimeType = mimeTypes.get(matcher.replaceAll(""));
				if(mimeType != null) {
					mimeType = mimeType.withCharset(charset);
				}
			} catch(UnsupportedCharsetException e) {
				// continue;
			}
		} else {
			mimeType = mimeTypes.get(contentType);
		}
		return mimeType;
	}

	public Collection<MimeType> types() {
		return mimeTypes.values();
	}
}
