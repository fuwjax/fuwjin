package com.echovantage.arrest.mime;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collection;

import org.echovantage.wonton.Wonton;

public interface MimeType {
	Charset charset();

	String contentType();

	String mimeType();

	Collection<Charset> charsets();

	MimeType withCharset(Charset charset);

	Wonton read(ByteBuffer input) throws IOException, ParseException;

	ByteBuffer write(Wonton result) throws IOException;
}
