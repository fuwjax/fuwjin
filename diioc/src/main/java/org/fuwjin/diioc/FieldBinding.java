package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.EXISTS;
import static org.fuwjin.diioc.Types.access;

import java.lang.reflect.Field;

import javax.inject.Provider;

public class FieldBinding implements Binding{
	private Key<?> key;
	private Object target;
	private Field field;

	public FieldBinding(Field field) {
		this(field, null);
	}

	public FieldBinding(Field field, Object target) {
		key = Key.keyOf(field.getName(),  field.getType(), field.getAnnotations());
		this.target = target;
		this.field = field;
	}

	@Override
	public Provider<?> provider(AbstractContext root) {
		return new Provider<Object>(){
			@Override
			public Object get() {
				try {
					return access(field).get(target);
				} catch (Exception e) {
					throw new ProviderException("Could not fetch falue from "+field.getName(), e);
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
		return EXISTS;
	}
}
