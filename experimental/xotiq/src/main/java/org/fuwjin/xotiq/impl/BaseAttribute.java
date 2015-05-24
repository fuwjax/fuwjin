package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Property;

public class BaseAttribute<M, V> extends AbstractAttribute<M,V> {
	private M message;
	private Property<M, V> property;

	protected BaseAttribute(M message, Property<M, V> property){
		this.message = message;
		this.property = property;
	}

	@Override
	public final M message() {
		return message;
	}
	
	@Override
	public final Property<M, V> property() {
		return property;
	}
}
