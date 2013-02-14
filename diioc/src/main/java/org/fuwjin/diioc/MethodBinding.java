package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.PROVIDED;
import static org.fuwjin.diioc.Types.access;

import java.lang.reflect.Method;

import javax.inject.Provider;

public class MethodBinding implements Binding {
	private Method method;
	private Object target;
	private Key<?> key;

	public MethodBinding(Method method) {
		this(method, null);
	}

	public MethodBinding(Method method, Object target) {
		this.method = method;
		this.target = target;
		key = Key.keyOf(method.getName(),  method.getReturnType(), method.getAnnotations());
	}

	@Override
	public Provider<?> provider(AbstractContext root) throws Exception {
		final Provider<Object[]> args = root.provideArray(method.getParameterTypes(), method.getParameterAnnotations());
		return new Provider<Object>(){
			@Override
			public Object get() {
				try {
					return access(method).invoke(target, args.get());
				} catch (Exception e) {
					throw new ProviderException("Could not invoke "+method.getName(), e);
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
		return PROVIDED;
	}
}
