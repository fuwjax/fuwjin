package org.fuwjin.diioc;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.inject.Scope;

public class ScopeContext implements Context{
	private static final Object CREATING = new Object();
	private Class<? extends Annotation> scope;
	private Map<Key<?>, Object> instances;

	public ScopeContext(Class<? extends Annotation> scope){
		if(!scope.isAnnotationPresent(Scope.class)){
			throw new IllegalArgumentException(scope+" must be an @Scope annotation");
		}
		this.scope = scope;
	}
	
	@Override
	public <T> T create(Diioc root, Key<T> key) throws Exception {
		T o = key.cast(instances.get(key));
		if(o == CREATING){
			return null;
		}
		if(o == null && key.isAnnotationPresent(scope)){
			instances.put(key, CREATING);
			try{
				o = root.create(root, key);
			}finally{
				instances.put(key, o);
			}
		}
		return o;
	}
}
