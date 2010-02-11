package org.fuwjin.gravitas.parser;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;

import org.fuwjin.gravitas.util.ClassUtils;
import org.fuwjin.pogo.reflect.invoke.Invoker;

class Var implements Atom{
	private String name;
	private Invoker invoker;
	private Invoker converter;

	@Override
	public boolean apply(Runnable runner, String value) {
		Object val = converter.invoke(null, value);
		Object result = invoker.invoke(runner, val);
		return isSuccess(result);
	}

	@Override
	public void resolve(Class<?> type) {
		invoker = new Invoker(type,name);
		Class<?> expectedType = invoker.paramTypes(1)[0];
		converter = new Invoker(ClassUtils.getWrapper(expectedType), "valueOf");
	}
	
	@Override
	public String toIdent(){
	   return '$'+name;
	}
}
