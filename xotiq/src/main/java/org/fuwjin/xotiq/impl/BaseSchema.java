package org.fuwjin.xotiq.impl;

import static com.google.common.collect.Iterables.unmodifiableIterable;
import static org.fuwjin.xotiq.impl.NullSchema.NULL_SCHEMA;

import java.util.concurrent.ConcurrentMap;

import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

public abstract class BaseSchema<M> implements TypedSchema<M> {
	private ConcurrentMap<Object, Property> properties = new ConcurrentLinkedHashMap.Builder<Object, Property>().maximumWeightedCapacity(Long.MAX_VALUE).build();

	public abstract Class<M> type();
	
	@Override
	public Schema parent() {
		return NULL_SCHEMA;
	}
	
	@Override
	public Property property(Object key) {
		Property prop = properties.get(key);
		if(prop == null){
			prop = addProperty(newProperty(key, createActor(key)));
		}
		return prop;
	}
	
	protected Object parentValueOf(M message, Object key){
		return parent().property(key).value(parentOf(message));
	}

	protected Object parentOf(M message){
		return null;
	}
	
	protected Property addProperty(Property prop) {
		Property old = properties.putIfAbsent(prop.key(), prop);
		if(old != null){
			prop = old;
		}
		return prop;
	}

	protected <V> BaseProperty<M, V> createProperty(Object key, PropertyActor<M, V> actor) {
		BaseProperty<M,V> property = newProperty(key, actor);
		addProperty(property);
		return property;
	}

	protected <V> BaseProperty<M, V> newProperty(Object key, PropertyActor<M, V> actor) {
		return new BaseProperty<M, V>(this, key, actor);
	}
	
	protected abstract PropertyActor<M, Object> createActor(Object key);

	@Override
	public Iterable<? extends Property> properties() {
		return unmodifiableIterable(properties.values());
	}

	@Override
	public String toString() {
		return "schema "+type().getName();
	}
	
	@Override
	public M convert(Object value) {
		return type().cast(value);
	}
}
