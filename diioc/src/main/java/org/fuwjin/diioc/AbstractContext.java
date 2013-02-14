package org.fuwjin.diioc;

import static org.fuwjin.diioc.Types.access;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Provider;

public abstract class AbstractContext implements Context{
	public <T> T create(Class<T> type, Annotation... qualifiers) throws Exception {
		return create(Key.keyOf(type, qualifiers));
	}

	public <T> T create(Key<T> key) throws Exception {
		return provide(key).get();
	}

	public <T> Provider<T> provide(Class<T> type, Annotation... qualifiers) throws Exception {
		return provide(Key.keyOf(type, qualifiers));
	}

	public <T> Provider<T> provide(Key<T> key) throws Exception {
		return bind(key).provider(this);
	}

	public <T> Binding<T> bind(Class<T> type, Annotation... qualifiers) throws Exception {
		return bind(Key.keyOf(type, qualifiers));
	}
	
	public <T> T inject(T instance) throws Exception{
		Class<?> type = instance.getClass();
		while(type != null){
			for(Field field: type.getDeclaredFields()){
				if(field.isAnnotationPresent(Inject.class)){
					access(field).set(instance, create(field.getType(), field.getAnnotations()));
				}
			}
			for(Method method: type.getDeclaredMethods()){
				if(method.isAnnotationPresent(Inject.class)){
					access(method).invoke(instance, createArray(method.getParameterTypes(), method.getParameterAnnotations()));
				}
			}
			type = type.getSuperclass();
		}
		return instance;
	}
	
	public Provider<Object[]> provideArray(Class<?>[] types, Annotation[][] qualifiers) throws Exception {
		final Provider<?>[] providers = new Provider<?>[types.length];
		for(int i=0;i<types.length;i++){
			providers[i] = provide(types[i], qualifiers[i]);
		}
		return new Provider<Object[]>(){
			@Override
			public Object[] get() {
				final Object[] array = new Object[providers.length];
				for(int i=0;i<array.length; i++){
					array[i] = providers[i].get();
				}
				return array;
			}
		};
	}

	public Object[] createArray(Class<?>[] types, Annotation[][] qualifiers) throws Exception {
		final Object[] array = new Object[types.length];
		for(int i=0;i<array.length; i++){
			array[i] = create(types[i], qualifiers[i]);
		}
		return array;
	}
}
