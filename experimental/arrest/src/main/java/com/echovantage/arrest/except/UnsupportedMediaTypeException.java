package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.UNSUPPORTED_MEDIA_TYPE;

public class UnsupportedMediaTypeException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedMediaTypeException() {
		super(UNSUPPORTED_MEDIA_TYPE);
	}
}
