package org.fuwjin.xotiq.impl;

import static com.google.common.collect.Iterables.unmodifiableIterable;

import java.util.concurrent.ConcurrentMap;

import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

public abstract class BaseSchema<M> extends AbstractSchema<M> {
	private ConcurrentMap<Object, Property<M,?>> properties = new ConcurrentLinkedHashMap.Builder<Object, Property<M,?>>().maximumWeightedCapacity(Long.MAX_VALUE).build();

	@Override
	public final <V> Property<M, V> property(Object key, Schema<V> type) {
		Property<M,?> prop = properties.get(key);
		if(prop == null){
			return unknownProperty(key, type);
		}
		return prop.as(type);
	}
	
	protected <V> Property<M,V> unknownProperty(Object key, Schema<V> type){
		return addProperty(key, new NullActor<M,V>(type));
	}

	protected final <V> Property<M, V> addProperty(Object key, PropertyActor<M, V> actor) {
		Property<M, V> property = newProperty(key, actor);
		Property<M,?> old = properties.putIfAbsent(property.key(), property);
		if(old != null){
			return old.as(property.value());
		}
		return property;
	}

	protected <V> BaseProperty<M, V> newProperty(Object key, PropertyActor<M, V> actor) {
		return new BaseProperty<M,V>(this, key, actor);
	}
	
	@Override
	public final Iterable<? extends Property<M,?>> properties() {
		return unmodifiableIterable(properties.values());
	}
}
