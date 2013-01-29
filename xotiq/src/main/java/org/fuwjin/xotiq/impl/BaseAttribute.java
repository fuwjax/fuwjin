package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Attribute;

public class BaseAttribute<M, V> implements Attribute {
	private M message;
	private BaseProperty<M, V> property;

	protected BaseAttribute(M message, BaseProperty<M, V> property){
		this.message = message;
		this.property = property;
	}

	@Override
	public M message() {
		return message;
	}

	@Override
	public BaseProperty<M, V> property() {
		return property;
	}
	
	@Override
	public V get() {
		return property().get(message());
	}

	@Override
	public V set(Object value) {
		return property().set(message(), value);
	}

	@Override
	public V clear() {
		return property().clear(message());
	}

	@Override
	public boolean has() {
		return property().has(message());
	}

}
