package org.fuwjin.diioc;

import static org.fuwjin.diioc.Diioc.access;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Provider;

public class ReflectiveContext implements Context {
	private Object context;

	public ReflectiveContext(Object context) {
		this.context = context;
	}
	
	@Override
	public <T> T create(Diioc root, Key<T> key) throws Exception {
		if(key.satisfies(null, context.getClass(), context.getClass().getAnnotations())){
			return key.cast(context);
		}
		Class<?> cls = context.getClass();
		while(cls != null){
			for(Field field: cls.getDeclaredFields()){
				if(key.satisfies(field.getName(), field.getType(), field.getAnnotations())){
					return key.cast(access(field).get(context));
				}
				if(Provider.class.isAssignableFrom(field.getType()) && key.satisfies(field.getName(), Key.parameter(field.getGenericType(),0), field.getAnnotations())){
					return key.cast(((Provider<?>)access(field).get(context)).get());
				}
			}
			for(Method method: cls.getDeclaredMethods()){
				if(method.isAnnotationPresent(Provides.class) && key.satisfies(method.getName(), method.getReturnType(), method.getAnnotations())){
					return key.cast(root.invoke(context, method));
				}
			}
			cls = cls.getSuperclass();
		}
		return null;
	}
}
