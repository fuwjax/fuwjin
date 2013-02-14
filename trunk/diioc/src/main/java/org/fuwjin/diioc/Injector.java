package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.NONE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import javax.inject.Inject;
import javax.inject.Singleton;

public class Injector extends AbstractContext{
	private final Class<? extends Annotation> scope;
	private AbstractContext[] contexts;
	private BindingMap bindings = new BindingMap();
	
	public Injector(Object... context){
		this(Singleton.class, context);
	}

	public Injector(Class<? extends Annotation> scope, Object... context){
		this.scope = scope;
		contexts = newContexts(context);
	}

	private static AbstractContext[] newContexts(Object... context) {
		AbstractContext[] c = new AbstractContext[context.length];
		int i = 0;
		for(Object o: context){
			c[i++] = newContext(o);
		}
		return c;
	}

	private Object[] createContext(Object... context) throws Exception {
		Object[] c = new Object[context.length+1];
		int i = 0;
		for(Object o: context){
			if(o instanceof Class){
				o = create((Class<?>)o);
			}
			c[i++] = o;
		}
		c[context.length] = this;
		return c;
	}
	
	public Injector newInjector(Object... context) throws Exception{
		return new Injector(createContext(context));
	}

	public Injector newInjector(Class<? extends Annotation> scope, Object... context) throws Exception{
		return new Injector(scope, createContext(context));
	}

	protected static AbstractContext newContext(Object o) {
		if(o instanceof AbstractContext){
			return (AbstractContext)o;
		}else if(o instanceof Context){
			return new ExtenedContext((Context)o);
		}
		return new ReflectiveContext(o);
	}
	
	@Override
	public <T> Binding<T> bind(Key<T> key) throws Exception {
		Binding<?> best = bindings.bind(key);
		if(best == null){
			best = buildBinding(key, best);
			bindings.add(best);
		}
		return (Binding<T>)best;
	}

	protected <T> Binding<?> buildBinding(Key<T> key, Binding<?> best) throws Exception {
		for(Context context: contexts){
			best = key.betterOf(context.bind(key), best);
		}
		if(best == null || best.strategy() == NONE){
			best = bindConstructors(key, best);
		}
		if(best == null){
			best = new NullBinding(key);
		}else if(best.strategy() != NONE && !(best instanceof SingleBinding) && hasScope(best, key)){
			best = new SingleBinding(best, this);
		}
		return best;
	}

	protected <T> Binding<?> bindConstructors(Key<T> key, Binding<?> best) {
		Constructor<?>[] constructors = key.type().getDeclaredConstructors();
		if(constructors.length == 1){
			best = key.betterOf(new ConstructorBinding(constructors[0]), best);
		}else if(constructors.length > 0){
			for(Constructor<?> c: constructors){
				if(c.isAnnotationPresent(Inject.class)){
					best = key.betterOf(new ConstructorBinding(c), best);
				}
			}
			if(best == null || best.strategy() == NONE){
				try{
					best = key.betterOf(new ConstructorBinding(key.type().getDeclaredConstructor()), best);
				}catch(NoSuchMethodException e){
					// continue
				}
			}
		}
		return best;
	}
	
	protected boolean hasScope(Binding<?> binding, Key<?> key){
		return binding.key().isAnnotationPresent(scope) || key.isAnnotationPresent(scope);
	}
}
	