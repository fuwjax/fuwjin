package org.fuwjin.gravitas.parser;

public class Literal implements Atom {
	private String value;

	@Override
	public boolean apply(Runnable runner, String arg) {
		return value.equals(arg);
	}
	
	@Override
	public void resolve(Class<?> type) {
		//ignore
	}
}
