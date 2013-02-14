package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.NONE;

import javax.inject.Provider;

public class NullBinding<T> implements Binding<T>{
	private Key<T> key;

	public NullBinding(Key<T> key) {
		this.key = key;
	}

	@Override
	public Provider<T> provider(AbstractContext root) {
		return new Provider<T>(){
			@Override
			public T get() {
				throw new ProviderException("No valid instance could be provided");
			}
		};
	}

	@Override
	public Key<T> key() {
		return key;
	}

	@Override
	public Strategy strategy() {
		return NONE;
	}

}
