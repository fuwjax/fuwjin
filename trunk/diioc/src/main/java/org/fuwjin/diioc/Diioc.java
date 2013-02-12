package org.fuwjin.diioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

public class Diioc implements Context{
	private Context[] contexts;

	public Diioc(Object... context){
		this.contexts = new Context[context.length];
		int i = 0;
		for(Object o: context){
			if(o instanceof Context){
				this.contexts[i++] = (Context)o;
			}else{
				this.contexts[i++] = new ReflectiveContext(o);
			}
		}
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
					invoke(instance, method);
				}
			}
			type = type.getSuperclass();
		}
		return instance;
	}

	public static <T extends AccessibleObject> T access(T obj) {
		if(!obj.isAccessible()){
			obj.setAccessible(true);
		}
		return obj;
	}

	public <T> T create(Class<T> type, Annotation... qualifiers) throws Exception {
		T o = create(this, new Key<T>(type, qualifiers));
		if(o != null){
			return o;
		}
		Constructor<?>[] constructors = type.getDeclaredConstructors();
		if(constructors.length == 1){
			return type.cast(invoke(constructors[0]));
		}
		for(Constructor<?> c: constructors){
			if(c.isAnnotationPresent(Inject.class)){
				return type.cast(invoke(c));
			}
		}
		return inject(access(type.getDeclaredConstructor()).newInstance());
	}
	
	@Override
	public <T> T create(Diioc root, Key<T> key) throws Exception {
		for(Context context: contexts){
			T obj = context.create(root, key);
			if(obj != null){
				return obj;
			}
		}
		return null;
	}

	public Object[] createArray(Class<?>[] types, Annotation[][] qualifiers) throws Exception {
		Object[] args = new Object[types.length];
		for(int i=0;i<args.length;i++){
			args[i] = create(types[i], qualifiers[i]);
		}
		return args;
	}
	
	public <T> T invoke(Constructor<T> c) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, Exception{
		return inject(access(c).newInstance(createArray(c.getParameterTypes(), c.getParameterAnnotations())));
	}

	public Object invoke(Object instance, Method method) throws Exception {
		return access(method).invoke(instance, createArray(method.getParameterTypes(), method.getParameterAnnotations()));
	}
}

