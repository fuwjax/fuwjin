package com.echovantage.arrest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.List;

import org.simpleframework.http.ContentType;
import org.simpleframework.http.Cookie;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;

public class HeadResponse implements Response {
	private final Response resp;

	public HeadResponse(final Response resp) {
		this.resp = resp;
	}

	@Override
	public List<String> getNames() {
		return resp.getNames();
	}

	@Override
	public void addValue(final String name, final String value) {
		resp.addValue(name, value);
	}

	@Override
	public void addInteger(final String name, final int value) {
		resp.addInteger(name, value);
	}

	@Override
	public void addDate(final String name, final long date) {
		resp.addDate(name, date);
	}

	@Override
	public void setValue(final String name, final String value) {
		resp.setValue(name, value);
	}

	@Override
	public void setInteger(final String name, final int value) {
		resp.setInteger(name, value);
	}

	@Override
	public void setLong(final String name, final long value) {
		resp.setLong(name, value);
	}

	@Override
	public void setDate(final String name, final long date) {
		resp.setDate(name, date);
	}

	@Override
	public String getValue(final String name) {
		return resp.getValue(name);
	}

	@Override
	public String getValue(final String name, final int index) {
		return resp.getValue(name, index);
	}

	@Override
	public int getInteger(final String name) {
		return resp.getInteger(name);
	}

	@Override
	public long getDate(final String name) {
		return resp.getDate(name);
	}

	@Override
	public List<String> getValues(final String name) {
		return resp.getValues(name);
	}

	@Override
	public Cookie setCookie(final Cookie cookie) {
		return resp.setCookie(cookie);
	}

	@Override
	public Cookie setCookie(final String name, final String value) {
		return resp.setCookie(name, value);
	}

	@Override
	public Cookie getCookie(final String name) {
		return resp.getCookie(name);
	}

	@Override
	public int getCode() {
		return resp.getCode();
	}

	@Override
	public void setCode(final int code) {
		resp.setCode(code);
	}

	@Override
	public String getDescription() {
		return resp.getDescription();
	}

	@Override
	public void setDescription(final String text) {
		resp.setDescription(text);
	}

	@Override
	public void setContentLength(final long length) {
		resp.setContentLength(length);
	}

	@Override
	public Status getStatus() {
		return resp.getStatus();
	}

	@Override
	public void setStatus(final Status status) {
		resp.setStatus(status);
	}

	@Override
	public int getMajor() {
		return resp.getMajor();
	}

	@Override
	public void setContentType(final String type) {
		resp.setContentType(type);
	}

	@Override
	public void setMajor(final int major) {
		resp.setMajor(major);
	}

	@Override
	public int getMinor() {
		return resp.getMinor();
	}

	@Override
	public void setMinor(final int minor) {
		resp.setMinor(minor);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		OutputStream out = resp.getOutputStream();
		return new OutputStream() {
			@Override
			public void write(final int b) throws IOException {
				// do nothing
			}

			@Override
			public void close() throws IOException {
				out.close();
			}

			@Override
			public void write(final byte[] b, final int off, final int len) throws IOException {
				// do nothing
			}
		};
	}

	@Override
	public OutputStream getOutputStream(final int size) throws IOException {
		return getOutputStream();
	}

	@Override
	public PrintStream getPrintStream() throws IOException {
		return new PrintStream(getOutputStream());
	}

	@Override
	public PrintStream getPrintStream(final int size) throws IOException {
		return new PrintStream(getOutputStream(size));
	}

	@Override
	public WritableByteChannel getByteChannel() throws IOException {
		return Channels.newChannel(getOutputStream());
	}

	@Override
	public WritableByteChannel getByteChannel(final int size) throws IOException {
		return Channels.newChannel(getOutputStream(size));
	}

	@Override
	public long getResponseTime() {
		return resp.getResponseTime();
	}

	@Override
	public boolean isKeepAlive() {
		return resp.isKeepAlive();
	}

	@Override
	public boolean isCommitted() {
		return resp.isCommitted();
	}

	@Override
	public List<Cookie> getCookies() {
		return resp.getCookies();
	}

	@Override
	public void commit() throws IOException {
		resp.commit();
	}

	@Override
	public ContentType getContentType() {
		return resp.getContentType();
	}

	@Override
	public String getTransferEncoding() {
		return resp.getTransferEncoding();
	}

	@Override
	public void reset() throws IOException {
		resp.reset();
	}

	@Override
	public long getContentLength() {
		return resp.getContentLength();
	}

	@Override
	public void close() throws IOException {
		resp.close();
	}

	@Override
	public CharSequence getHeader() {
		return resp.getHeader();
	}

	@Override
	public String toString() {
		return resp.toString();
	}
}
