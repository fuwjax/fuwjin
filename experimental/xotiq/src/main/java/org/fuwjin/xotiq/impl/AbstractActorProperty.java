package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Schema;

public abstract class AbstractActorProperty<M, V> extends AbstractProperty<M, V> {
	@Override
	public final V get(M message) {
		return has(message) ? actor().get(message) : parentValue(message);
	}
	
	@Override
	public final boolean has(M message){
		return actor().has(message);
	}
	
	protected abstract PropertyActor<M, V> actor();

	@Override
	public final V set(M message, V value){
		V old = get(message);
		actor().set(message, value);
		return old;
	}
	
	@Override
	public final V clear(M message){
		V old = get(message);
		actor().clear(message);
		return old;
	}
	
	@Override
	public final Schema<V> value() {
		return actor().type();
	}
}
