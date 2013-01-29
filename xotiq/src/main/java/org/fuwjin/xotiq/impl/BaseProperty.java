package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public class BaseProperty<M, V> implements Property {
	private Object key;
	private BaseSchema<M> schema;
	private PropertyActor<M, V> actor;

	protected BaseProperty(BaseSchema<M> schema, Object key, PropertyActor<M, V> actor){
		this.schema = schema;
		this.key = key;
		this.actor = actor;
	}
	
	@Override
	public Object key() {
		return key;
	}

	@Override
	public Schema schema() {
		return schema;
	}
	
	@Override
	public Attribute attribute(Object message) {
		if(schema.type().isInstance(message)){
			M msg = schema.type().cast(message);
			return new BaseAttribute<M,V>(msg, this);
		}
		if(message instanceof Message){
			return ((Message)message).attribute(key);
		}
		return new NullAttribute(this);
	}
	
	@Override
	public String toString() {
		return schema.type().getName()+": "+key;
	}

	public V get(M message) {
		return has(message) ? actor().get(message) : actor().convert(schema.parentValueOf(message, key));
	}
	
	public boolean has(M message){
		return actor().has(message);
	}
	
	protected PropertyActor<M, V> actor() {
		return actor;
	}

	public V set(M message, Object value){
		V old = get(message);
		actor().set(message, actor().convert(value));
		return old;
	}
	
	public V clear(M message){
		V old = get(message);
		actor().clear(message);
		return old;
	}

	@Override
	public Object value(Object message) {
		if(schema.type().isInstance(message)){
			M msg = schema.type().cast(message);
			return get(msg);
		}
		if(message instanceof Message){
			return ((Message)message).attribute(key).get();
		}
		if(message == null){
			throw new UnsupportedOperationException("null has no properties");
		}
		throw new UnsupportedOperationException("unknown type: "+message.getClass());
	}
}
