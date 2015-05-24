package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.NOT_FOUND;

public class NotFoundException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException() {
		super(NOT_FOUND);
	}
}
