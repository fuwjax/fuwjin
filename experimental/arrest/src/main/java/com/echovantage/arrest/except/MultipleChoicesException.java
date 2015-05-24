package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.MULTIPLE_CHOICES;

public class MultipleChoicesException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MultipleChoicesException() {
		super(MULTIPLE_CHOICES);
	}
}
