package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.NOT_ACCEPTABLE;

public class NotAcceptableException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NotAcceptableException() {
		super(NOT_ACCEPTABLE);
	}
}
