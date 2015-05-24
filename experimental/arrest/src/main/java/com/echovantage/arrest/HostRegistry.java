package com.echovantage.arrest;

import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.except.BadRequestException;
import com.echovantage.util.PatternRegistry;

public class HostRegistry {
	private final PatternRegistry<Host> registry = new PatternRegistry<>();

	public HostRegistry register(final String pattern, final Host target) {
		registry.register(pattern, target);
		return this;
	}

	public Host get(final String host) throws ArrestException {
		final Host api = registry.get(host);
		if(api == null) {
			throw new BadRequestException();
		}
		return api;
	}
}
