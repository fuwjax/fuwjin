package com.echovantage.util;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
	private final ByteBuffer buffer;
	private int mark;

	public ByteBufferInputStream(final ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int read() {
		return buffer.hasRemaining() ? buffer.get() & 0xFF : -1;
	}

	@Override
	public int read(final byte[] b) {
		return read(b, 0, b.length);
	}

	@Override
	public int read(final byte[] b, final int off, final int len) {
		if(!buffer.hasRemaining()) {
			return -1;
		}
		int length = Math.min(len, buffer.remaining());
		buffer.get(b, off, length);
		return length;
	}

	@Override
	public int available() {
		return buffer.remaining();
	}

	@Override
	public long skip(final long n) {
		if(n <= 0) {
			return 0;
		}
		int length = (int) Math.min(n, buffer.remaining());
		buffer.position(buffer.position() + length);
		return length;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void mark(final int readlimit) {
		mark = buffer.position();
	}

	@Override
	public synchronized void reset() {
		buffer.position(mark);
	}

	@Override
	public void close() {
		// do nothing
	}
}
