package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Schema;

public class BaseProperty<M, V> extends AbstractActorProperty<M, V> {
	private Object key;
	private Schema<M> schema;
	private PropertyActor<M, V> actor;

	protected BaseProperty(Schema<M> schema, Object key, PropertyActor<M, V> actor){
		this.schema = schema;
		this.key = key;
		this.actor = actor;
	}
	
	@Override
	public final Object key() {
		return key;
	}

	@Override
	public final Schema<M> schema() {
		return schema;
	}
	
	protected final PropertyActor<M, V> actor() {
		return actor;
	}
}
