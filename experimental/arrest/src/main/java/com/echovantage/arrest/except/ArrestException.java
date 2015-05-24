package com.echovantage.arrest.except;

import org.simpleframework.http.Response;
import org.simpleframework.http.Status;

import com.echovantage.arrest.MethodResult;

public class ArrestException extends Exception implements MethodResult {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Status status;

	public ArrestException(final Status status) {
		super(status.description);
		this.status = status;
	}

	@Override
	public void apply(final Response resp) {
		resp.setStatus(status);
	}
}
