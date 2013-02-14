package org.fuwjin.diioc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.inject.Provider;

public class ReflectiveContext extends AbstractContext {
	private BindingMap bindings = new BindingMap();

	public ReflectiveContext(Object context) {
		bindings.add(new ConstantBinding<Object>(context));
		Class<?> cls = context.getClass();
		while(cls != null){
			for(final Field field: cls.getDeclaredFields()){
				if(field.isSynthetic()){
					continue;
				}
				if(Provider.class.isAssignableFrom(field.getType())){
					if(Modifier.isStatic(field.getModifiers())){
						bindings.add(new ProviderFieldBinding(field));
					}else{
						bindings.add(new ProviderFieldBinding(field, context));
					}
				}else{
					if(Modifier.isStatic(field.getModifiers())){
						bindings.add(new FieldBinding(field));
					}else{
						bindings.add(new FieldBinding(field, context));
					}
				}
			}
			for(final Method method: cls.getDeclaredMethods()){
				if(method.isAnnotationPresent(Provides.class)){
					if(Modifier.isStatic(method.getModifiers())){
						bindings.add(new MethodBinding(method));
					}else{
						bindings.add(new MethodBinding(method, context));
					}
				}
			}
			cls = cls.getSuperclass();
		}
	}

	@Override
	public <T> Binding<T> bind(Key<T> key) throws Exception {
		return bindings.bind(key);
	}
}
