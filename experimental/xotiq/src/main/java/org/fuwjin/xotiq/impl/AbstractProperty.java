package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public abstract class AbstractProperty<M, V> implements Property<M, V> {
	@Override
	public Attribute<M,V> attribute(M message) {
		return new BaseAttribute<M,V>(message, this);
	}
	
	@Override
	public final <O> Property<M, O> as(Schema<O> type) {
		if(value().equals(type)){
			return (Property<M,O>)this;
		}
		return asImpl(type);
	}

	protected <O> Property<M, O> asImpl(Schema<O> type) {
		return new ValueConversionProperty<M,V,O>(this, type);
	}
	
	@Override
	public final <O> Attribute<M, O> attribute(M message, Schema<O> type) {
		return as(type).attribute(message);
	}
	
	@Override
	public String toString() {
		return schema()+": "+key();
	}
	
	protected final V parentValue(M message) {
		Message<?> parent = schema().parentOf(message);
		return parent == null ? null : value().convert(parent.get(key()));
	}
}
