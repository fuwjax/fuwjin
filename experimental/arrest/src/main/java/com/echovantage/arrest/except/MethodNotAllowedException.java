package com.echovantage.arrest.except;

import static org.simpleframework.http.Protocol.ALLOW;
import static org.simpleframework.http.Status.METHOD_NOT_ALLOWED;

import org.simpleframework.http.Response;

import com.echovantage.arrest.Resource;
import com.echovantage.util.Streams;

public class MethodNotAllowedException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Resource resource;

	public MethodNotAllowedException(final Resource resource) {
		super(METHOD_NOT_ALLOWED);
		this.resource = resource;
	}

	@Override
	public void apply(final Response resp) {
		resp.setValue(ALLOW, Streams.join(",", resource.options()));
		super.apply(resp);
	}
}
