package org.fuwjin.gravitas.util;

import static java.lang.reflect.Proxy.newProxyInstance;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;

public class ClassUtils {
	public static <A extends Annotation> A getAnnotation(Class<?> type,
			Class<A> annotation) {
		while (type != null) {
			A ann = type.getAnnotation(annotation);
			if (ann != null) {
				return ann;
			}
			type = type.getSuperclass();
		}
		return newProxy(annotation, new AnnotationHandler(annotation));
	}

	public static <T> T newProxy(Class<T> type, InvocationHandler handler) {
		return type.cast(newProxyInstance(type.getClassLoader(),
				new Class<?>[] { type }, handler));
	}
}
