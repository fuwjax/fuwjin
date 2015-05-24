package com.echovantage.arrest.except;

import com.echovantage.util.Streams;
import org.simpleframework.http.Response;

import java.util.Collection;

import static org.simpleframework.http.Protocol.ALLOW;
import static org.simpleframework.http.Status.NOT_IMPLEMENTED;

public class NotImplementedException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Collection<?> options;

	public NotImplementedException(final Collection<?> options) {
		super(NOT_IMPLEMENTED);
		this.options = options;
	}

	@Override
	public void apply(final Response resp) {
		resp.setValue(ALLOW, Streams.join(",", options));
		super.apply(resp);
	}
}
