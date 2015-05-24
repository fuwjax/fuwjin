package com.echovantage.arrest;

import java.util.Collection;
import java.util.Date;

import com.echovantage.arrest.except.ArrestException;
import com.echovantage.arrest.except.MethodNotAllowedException;

public interface Resource {
	String location();

	String containerLocation();

	Long tag();

	Date lastModified();

	Resource locate(final String name);

	Collection<String> options();

	Collection<String> names();

	MethodAction getAction(Host.Method methodId) throws ArrestException;
}
