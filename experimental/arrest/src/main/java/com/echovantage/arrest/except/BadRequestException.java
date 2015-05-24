package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.BAD_REQUEST;

public class BadRequestException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BadRequestException() {
		super(BAD_REQUEST);
	}
}
