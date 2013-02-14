package org.fuwjin.diioc;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

public class Types {
	public static <T extends AccessibleObject> T access(T obj) {
		if(!obj.isAccessible()){
			obj.setAccessible(true);
		}
		return obj;
	}

	public static Class<?> parameter(Type generic, int index) {
		return (Class<?>)((ParameterizedType)generic).getActualTypeArguments()[index];
	}
	
	public static <T> T proxy(Class<T> type, InvocationHandler handler){
		return type.cast(Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler));
	}
	
	public static boolean moreSpecific(Class<?> target, Class<?> current, Class<?> test){
		if(target == null){
			return false;
		}
		if(test == null || !target.isAssignableFrom(test)){
			return false;
		}
		if(current == null || !target.isAssignableFrom(current)){
			return true;
		}
		if(current.isAssignableFrom(test)){
			return false;
		}
		if(test.isAssignableFrom(current)){
			return true;
		}
		if(current.isInterface() && !test.isInterface()){
			return true;
		}
		return false;
	}
}
