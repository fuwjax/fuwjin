package org.fuwjin.diioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Named;
import javax.inject.Qualifier;

public class Key<T> {
	private Class<T> type;
	private Annotation[] qualifiers;

	public Key(Class<T> type, Annotation... qualifiers){
		this.type = type;
		this.qualifiers = qualifiers;
		
	}

	public boolean satisfies(String name, Class<?> cls, Annotation... annotations) {
		return type.isAssignableFrom(cls) && satisfies(annotations, name);
	}

	private boolean satisfies(Annotation qualifier, Annotation[] annotations, String implicitName) {
		for(Annotation annotation: annotations){
			if(qualifier.equals(annotation)){
				return true;
			}
		}
		if(qualifier instanceof Named){
			return ((Named)qualifier).value().equals(implicitName);
		}
		return false;
	}

	private boolean satisfies(Annotation[] annotations, String implicitName) {
		for(Annotation qualifier: qualifiers){
			if(qualifier instanceof Qualifier && !satisfies(qualifier, annotations, implicitName)){
				return false;
			}
		}
		return true;
	}

	public static Class<?> parameter(Type generic, int index) {
		return (Class<?>)((ParameterizedType)generic).getActualTypeArguments()[index];
	}

	public Class<T> type() {
		return type;
	}
	
	public T cast(Object o){
		return type.cast(o);
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> scope) {
		if(type.isAnnotationPresent(scope)){
			return true;
		}
		for(Annotation qualifier: qualifiers){
			if(scope.isInstance(qualifier)){
				return true;
			}
		}
		return false;
	}
}
