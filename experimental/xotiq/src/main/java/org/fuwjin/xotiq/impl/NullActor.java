package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Schema;

public class NullActor<M,V> implements PropertyActor<M,V> {
	private Schema<V> type;

	public NullActor(Schema<V> type){
		this.type = type;
	}

	@Override
	public V get(M message) {
		return null;
	}

	@Override
	public void set(M message, V value) {
		throw new IllegalStateException("No such property");
	}

	@Override
	public void clear(M message) {
	}

	@Override
	public boolean has(M message) {
		return false;
	}

	@Override
	public Schema<V> type() {
		return type;
	}
}