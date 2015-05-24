package com.echovantage.arrest.result;

import static org.simpleframework.http.Status.NO_CONTENT;

import org.simpleframework.http.Response;

import com.echovantage.arrest.MethodResult;

public class NoResult implements MethodResult {
	@Override
	public void apply(final Response resp) {
		resp.setContentLength(0);
		resp.setStatus(NO_CONTENT);
	}
}
