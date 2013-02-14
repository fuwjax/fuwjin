package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.EXISTS;

import javax.inject.Provider;

public class ConstantBinding<T> implements Binding<T> {
	private T constant;
	private Key<T> key;

	public ConstantBinding(T constant) {
		this.constant = constant;
		key = Key.keyOf((Class<T>)constant.getClass());
	}

	@Override
	public Provider<T> provider(AbstractContext root) {
		return new Provider<T>(){
			@Override
			public T get() {
				return constant;
			}
		};
	}

	@Override
	public Key<T> key() {
		return key;
	}
	
	@Override
	public Strategy strategy() {
		return EXISTS;
	}
}
