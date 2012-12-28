package org.fuwjin.diioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Scope;
import javax.inject.Singleton;

public class Diioc {
	private Class<? extends Annotation> scope;

	public Diioc(){
		this(Singleton.class);
	}
	
	public Diioc(Class<? extends Annotation> scope){
		if(!scope.isAnnotationPresent(Scope.class)){
			throw new IllegalArgumentException(scope+" must be an @Scope annotation");
		}
		this.scope = scope;
	}
	
	public <T> T create(Class<T> type) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return create(type, null);
	}

	private <T extends AccessibleObject> T access(T obj) {
		if(!obj.isAccessible()){
			obj.setAccessible(true);
		}
		return obj;
	}

	public <T> T injectMembers(T instance, Object context) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		Class<?> type = instance.getClass();
		while(type != null){
			for(Field field: type.getDeclaredFields()){
				if(field.isAnnotationPresent(Inject.class)){
					access(field).set(instance, create(field.getType(), context));
				}
			}
			for(Method method: type.getDeclaredMethods()){
				if(method.isAnnotationPresent(Inject.class)){
					access(method).invoke(instance, createArray(method.getParameterTypes(), context));
				}
			}
			type = type.getSuperclass();
		}
		return instance;
	}

	private Object[] createArray(Class<?>[] types, Object context) throws InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object[] args = new Object[types.length];
		for(int i=0;i<args.length;i++){
			args[i] = create(types[i], context);
		}
		return args;
	}

	public <T> T create(Class<T> type, Object context) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(context != null){
			Class<?> cls = context.getClass();
			while(cls != null){
				for(Field field: cls.getDeclaredFields()){
					if(type.isAssignableFrom(field.getType()) && field.isAnnotationPresent(scope)){
						return (T)access(field).get(context);
					}
					if(Provider.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType && type.isAssignableFrom(((Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]))){
						return ((Provider<T>)access(field).get(context)).get();
					}
				}
				for(Method method: cls.getDeclaredMethods()){
					if(type.isAssignableFrom(method.getReturnType()) && method.isAnnotationPresent(Provides.class)){
						return (T)access(method).invoke(context, createArray(method.getParameterTypes(), context));
					}
				}
				cls = cls.getSuperclass();
			}
		}
		for(Constructor<?> c: type.getDeclaredConstructors()){
			if(c.isAnnotationPresent(Inject.class)){
				return injectMembers((T)access(c).newInstance(createArray(c.getParameterTypes(), context)), context);
			}
		}
		return injectMembers(access(type.getDeclaredConstructor()).newInstance(), context);
	}
}
