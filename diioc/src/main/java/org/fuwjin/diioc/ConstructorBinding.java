package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.CREATED;
import static org.fuwjin.diioc.Types.access;

import java.lang.reflect.Constructor;

import javax.inject.Provider;

public class ConstructorBinding implements Binding {
	private Constructor<?> constructor;
	private Key<?> key;

	public ConstructorBinding(Constructor<?> constructor) {
		this.constructor = constructor;
		key = Key.keyOf(constructor.getName(), constructor.getDeclaringClass(), constructor.getAnnotations());
	}

	@Override
	public Provider<?> provider(final AbstractContext root) throws Exception {
		final Provider<Object[]> args = root.provideArray(constructor.getParameterTypes(), constructor.getParameterAnnotations());
		return new Provider<Object>(){
			@Override
			public Object get() {
				try {
					return root.inject(access(constructor).newInstance(args.get()));
				} catch (Exception e) {
					throw new ProviderException("Could not invoke constructor", e);
				}
			}
		};
	}

	@Override
	public Key<?> key() {
		return key;
	}
	
	@Override
	public Strategy strategy() {
		return CREATED;
	}
}
