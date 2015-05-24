package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Schema;

public abstract class AbstractMessage<M extends Message<M>> implements Message<M> {
	@Override
	public final Object get(Object key) {
		return schema().property(key).get(self());
	}
	
	protected final M self(){
		return (M)this;
	}

	@Override
	public final Attribute<M, ?> attribute(Object key) {
		return schema().property(key).attribute(self());
	}

	@Override
	public final <V> Attribute<M, V> attribute(Object key,
			Schema<V> type) {
		return schema().property(key, type).attribute(self());
	}

	@Override
	public final Iterable<? extends Attribute<M, ?>> attributes() {
		return schema().attributes(self());
	}
}
