package org.fuwjin.xotiq.impl;

import java.util.Collections;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public abstract class AbstractSchema<M> implements Schema<M> {
	public Property<M, ?> property(Object key){
		return property(key, defaultPropertyValue());
	}

	protected Schema<?> defaultPropertyValue() {
		return PrimitiveSchema.OBJECT;
	}
	
	@Override
	public M create(Message<?> parent) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Message<?> parentOf(M message) {
		return null;
	}
	
	@Override
	public <V> Property<M, V> property(Object key, Schema<V> type) {
		return property(key).as(type);
	}

	@Override
	public Iterable<? extends Attribute<M, ?>> attributes(final M message) {
		return Iterables.transform(properties(), new Function<Property<M,?>, Attribute<M,?>>(){
			public Attribute<M,?> apply(Property<M,?> property){
				return property.attribute(message);
			}
		});
	}
	
	@Override
	public Iterable<? extends Property<M, ?>> properties() {
		return Collections.emptyList();
	}

	public String toString(M message) {
		StringBuilder builder = new StringBuilder("{");
		for(Property<M,?> property: properties()){
			Object value = property.get(message);
			if(value != null){
				if(builder.length() > 1){
					builder.append(", ");
				}
				builder.append(property.key()).append("=").append(value);
			}
		}
		return builder.append("}").toString();
	}
}
