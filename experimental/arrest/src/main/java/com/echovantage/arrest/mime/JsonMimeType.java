package com.echovantage.arrest.mime;

import com.echovantage.util.Charsets;
import org.echovantage.metafactory.MetaService;
import org.echovantage.wonton.Wonton;
import org.echovantage.wonton.WontonParser;
import org.echovantage.wonton.WontonSerial;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;

@MetaService(MimeType.class)
public class JsonMimeType extends AbstractMimeType {
    @Override
    public Charset charset() {
        return Charsets.UTF_8;
    }

    @Override
    public String mimeType() {
        return "application/json";
    }

    @Override
    public String contentType(final Charset charset) {
        return mimeType();
    }

    @Override
    public Collection<Charset> charsets() {
        return Collections.singleton(Charsets.UTF_8);
    }

    @Override
    public Wonton read(final ByteBuffer input) throws IOException, ParseException {
        return new WontonParser(input).parse();
    }

    @Override
    public ByteBuffer write(final Wonton result) throws IOException {
        return ByteBuffer.wrap(WontonSerial.toString(result).getBytes(charset()));
    }
}
