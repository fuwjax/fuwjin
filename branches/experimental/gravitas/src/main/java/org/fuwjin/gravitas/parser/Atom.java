package org.fuwjin.gravitas.parser;

interface Atom {
	boolean apply(Runnable runner, String value);

	void resolve(Class<?> type);
}
