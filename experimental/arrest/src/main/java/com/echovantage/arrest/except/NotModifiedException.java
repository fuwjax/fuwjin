package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.NOT_MODIFIED;

public class NotModifiedException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NotModifiedException() {
		super(NOT_MODIFIED);
	}
}
