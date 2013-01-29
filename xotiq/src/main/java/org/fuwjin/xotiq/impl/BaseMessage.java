package org.fuwjin.xotiq.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

import com.google.common.base.Objects;

public class BaseMessage<S extends Schema> implements Message {
	private S schema;
	private Object parent;
	private ConcurrentMap<Object, Attribute> attributes = new ConcurrentHashMap<Object, Attribute>();

	protected BaseMessage(S schema){
		this.schema = schema;
		this.parent = schema.parent().create();
	}
	
	protected BaseMessage(S schema, Object parent){
		this.schema = schema;
		this.parent = parent;
	}

	@Override
	public Object parent() {
		return parent;
	}

	@Override
	public S schema() {
		return schema;
	}
	
	@Override
	public Attribute attribute(Object key) {
		Attribute attr = attributes.get(key);
		if(attr == null){
			attr = schema().property(key).attribute(this);
			Attribute old = attributes.putIfAbsent(key, attr);
			if(old != null){
				attr = old;
			}
		}
		return attr;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		for(Property property: schema().properties()){
			Object value = property.value(this);
			if(value != null){
				if(builder.length() > 1){
					builder.append(", ");
				}
				builder.append(property.key()).append("=").append(value);
			}
		}
		return builder.append("}").toString();
	}
	
	@Override
	public int hashCode() {
		int hash = schema.hashCode();
		for(Property property: schema().properties()){
			Object value = property.value(this);
			if(value != null){
				hash = hash *31 + value.hashCode();
			}
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !schema.type().equals(obj.getClass())){
			return false;
		}
		for(Property property: schema().properties()){
			if(!Objects.equal(property.value(this),property.value(obj))){
				return false;
			}
		}
		return true;
	}
}

