package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.LENGTH_REQUIRED;

public class LengthRequiredException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public LengthRequiredException() {
		super(LENGTH_REQUIRED);
	}
}
