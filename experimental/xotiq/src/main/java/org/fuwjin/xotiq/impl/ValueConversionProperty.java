package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public class ValueConversionProperty<M, V, O> extends AbstractProperty<M, O> {
	private Property<M, V> property;
	private Schema<O> valueType;

	public ValueConversionProperty(Property<M, V> property, Schema<O> type) {
		this.property = property;
		this.valueType = type;
	}

	@Override
	public Object key() {
		return property.key();
	}

	@Override
	public Schema<M> schema() {
		return property.schema();
	}
	
	@Override
	protected <NO> Property<M, NO> asImpl(Schema<NO> type) {
		return property.as(type);
	}

	@Override
	public O get(M message) {
		return convertFrom(property.get(message));
	}

	protected O convertFrom(V value) {
		return value().convert(value);
	}

	@Override
	public O set(M message, O value) {
		return convertFrom(property.set(message, convertTo(value)));
	}

	protected V convertTo(O value) {
		return property.value().convert(value);
	}

	@Override
	public O clear(M message) {
		return convertFrom(property.clear(message));
	}

	@Override
	public boolean has(M message) {
		return property.has(message);
	}

	@Override
	public Schema<O> value() {
		return valueType;
	}
}
