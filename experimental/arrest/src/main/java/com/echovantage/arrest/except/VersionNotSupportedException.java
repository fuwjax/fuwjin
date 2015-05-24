package com.echovantage.arrest.except;

import static org.simpleframework.http.Status.VERSION_NOT_SUPPORTED;

public class VersionNotSupportedException extends ArrestException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VersionNotSupportedException() {
		super(VERSION_NOT_SUPPORTED);
	}
}
