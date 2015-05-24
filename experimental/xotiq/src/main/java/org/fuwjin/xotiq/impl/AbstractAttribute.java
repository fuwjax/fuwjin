package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Schema;

public abstract class AbstractAttribute<M, V> implements Attribute<M,V> {
	@Override
	public final <O> Attribute<M, O> as(Schema<O> type) {
		return property().as(type).attribute(message());
	}
	
	@Override
	public final V get() {
		return property().get(message());
	}

	@Override
	public final V set(V value) {
		return property().set(message(), value);
	}

	@Override
	public final V clear() {
		return property().clear(message());
	}

	@Override
	public final boolean has() {
		return property().has(message());
	}

}
