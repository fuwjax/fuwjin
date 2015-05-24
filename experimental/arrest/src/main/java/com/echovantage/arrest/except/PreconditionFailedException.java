package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.PRECONDITION_FAILED;

public class PreconditionFailedException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PreconditionFailedException() {
		super(PRECONDITION_FAILED);
	}
}
