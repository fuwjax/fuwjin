package org.fuwjin.diioc;

import static org.fuwjin.diioc.Binding.Strategy.EXISTS;
import static org.fuwjin.diioc.Binding.Strategy.NONE;
import static org.fuwjin.diioc.Binding.Strategy.PROVIDED;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.inject.Named;
import javax.inject.Qualifier;

import org.fuwjin.diioc.Binding.Strategy;

public class Key<T> {
	private Class<T> type;
	private Annotation[] qualifiers;

	public Key(Class<T> type, Annotation... qualifiers){
		this.type = type;
		this.qualifiers = qualifiers;
	}
	
	public Binding<?> betterOf(Binding<?> target, Binding<?> best) {
		if(target == null){
			return best;
		}
		if(best == null && target.strategy() == NONE){
			return target;
		}
		if(!type.isAssignableFrom(target.key().type())){
			return best;
		}
		for(Annotation qualifier: qualifiers){
			if(isQualifier(qualifier) && !target.key().hasQualifier(qualifier)){
				return best;
			}
		}
		if(best == null){
			return target;
		}
		switch(best.strategy()){
		case NONE:
			return target;
		case CREATED:
			if(target.strategy() == EXISTS || target.strategy() == PROVIDED){
				break;
			}
			return best;
		case PROVIDED:
			if(target.strategy() == EXISTS && !target.key().type().equals(type)){
				break;
			}else if(target.strategy() == PROVIDED && Types.moreSpecific(type, best.key().type(), target.key().type())){
				break;
			}
			return best;
		case EXISTS:
			if(target.strategy() == PROVIDED && target.key().type().equals(type) && !best.key().type().equals(type)){
				break;
			}else if(target.strategy() == EXISTS && Types.moreSpecific(type, best.key().type(), target.key().type())){
				break;
			}
			return best;
		}
		if(target.key().countQualifiers() >= best.key().countQualifiers()){
			return best;
		}
		return target;
	}
	
	protected int countQualifiers(){
		int count = 0;
		for(Annotation annotation: qualifiers){
			if(isQualifier(annotation)){
				count++;
			}
		}
		return count;
	}

	protected boolean isQualifier(Annotation qualifier) {
		return qualifier.annotationType().isAnnotationPresent(Qualifier.class);
	}

	protected boolean hasQualifier(Annotation qualifier) {
		for(Annotation annotation: qualifiers){
			if(qualifier.equals(annotation)){
				return true;
			}
		}
		return false;
	}

	public Class<T> type() {
		return type;
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

	public Annotation[] qualifiers() {
		return qualifiers;
	}
	
	public static <T> Key<T> keyOf(Class<T> type, Annotation... qualifiers) {
		return new Key<T>(type, qualifiers);
	}
	
	public static <T> Key<T> keyOf(String name, Class<T> type, Annotation... qualifiers) {
		for(Annotation qualifier: qualifiers){
			if(qualifier instanceof Named){
				return keyOf(type, qualifiers);
			}
		}
		qualifiers = Arrays.copyOf(qualifiers, qualifiers.length + 1);
		qualifiers[qualifiers.length - 1] = AnnotationBuilder.newAnnotation(Named.class).with("value",name).build();
		return keyOf(type, qualifiers);
	}
}
