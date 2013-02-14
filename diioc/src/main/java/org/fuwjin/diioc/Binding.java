package org.fuwjin.diioc;

import javax.inject.Provider;

public interface Binding<T> {
	public enum Strategy{
		NONE, EXISTS, PROVIDED, CREATED
	}

	Provider<T> provider(AbstractContext root) throws Exception;

	Key<T> key();
	
	Strategy strategy();
}
