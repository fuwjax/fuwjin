package org.fuwjin.gravitas.parser;

import java.util.HashMap;
import java.util.Map;

public class Parser {
	private Map<Class<?>,Context> contexts = new HashMap<Class<?>,Context>();
	private ClassResolver resolver;
	
	void addContext(Context context){
		context.resolve(resolver);
		contexts.put(context.type(), context);
	}
	
	public Context getContext(Object forObject){
		return contexts.get(forObject.getClass());
	}
}
