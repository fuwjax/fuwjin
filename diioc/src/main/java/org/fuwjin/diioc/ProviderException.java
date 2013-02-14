package org.fuwjin.diioc;

public class ProviderException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProviderException(String message) {
		super(message);
	}
}
