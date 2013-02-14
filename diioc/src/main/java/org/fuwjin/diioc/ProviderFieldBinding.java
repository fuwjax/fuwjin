package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.PROVIDED;
import static org.fuwjin.diioc.Types.access;

import java.lang.reflect.Field;

import javax.inject.Provider;

public class ProviderFieldBinding implements Binding{
	private Key<?> key;
	private Field field;
	private Object target;

	public ProviderFieldBinding(Field field) {
		this(field, null);
	}

	public ProviderFieldBinding(Field field, Object target) {
		assert Provider.class.isAssignableFrom(field.getType());
		key = Key.keyOf(field.getName(), Types.parameter(field.getGenericType(),0), field.getAnnotations());
		this.field = field;
		this.target = target;
	}

	@Override
	public Provider<?> provider(AbstractContext root) {
		try {
			return (Provider<?>)access(field).get(target);
		} catch (Exception e) {
			throw new ProviderException("Could not fetch falue from "+field.getName(), e);
		}
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
