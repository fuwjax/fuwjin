package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public class MessageConversionProperty<M, S, V> extends AbstractProperty<M, V> {
	private Property<S, V> property;
	private Schema<M> schema;

	public MessageConversionProperty(Schema<M> schema, Property<S, V> property) {
		this.schema = schema;
		this.property = property;
	}

	@Override
	public final Object key() {
		return property.key();
	}

	@Override
	public final Schema<M> schema() {
		return schema;
	}

	protected S convert(M message){
		return property.schema().convert(message);
	}

	@Override
	public final V get(M message) {
		return property.get(convert(message));
	}

	@Override
	public final V set(M message, V value) {
		return property.set(convert(message), value);
	}

	@Override
	public final V clear(M message) {
		return property.clear(convert(message));
	}

	@Override
	public final boolean has(M message) {
		return property.has(convert(message));
	}

	@Override
	public final Schema<V> value() {
		return property.value();
	}
}
