package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public abstract class PrimitiveSchema<M> extends AbstractSchema<M> {
	public static final Schema<Object> OBJECT = new PrimitiveSchema<Object>(){
		@Override
		public Object convert(Object value) {
			return value;
		}
		
		@Override
		public boolean isMessage(Object message) {
			return true;
		}
	};
	
	public static final Schema<Void> VOID = new PrimitiveSchema<Void>(){
		public Void convert(Object value) {
			return null;
		}
		
		@Override
		public boolean isMessage(Object message) {
			return message == null;
		}
	};
	
	public static final Schema<String> STRING = new PrimitiveSchema<String>(){
		public String convert(Object value) {
			return value == null ? null : value.toString();
		}
		
		@Override
		public boolean isMessage(Object message) {
			return message instanceof String;
		}
	};

	public static final Schema<Integer> INTEGER = new PrimitiveSchema<Integer>(){
		@Override
		public Integer convert(Object value) {
			return value == null ? null : value instanceof Number ? ((Number)value).intValue() : Integer.valueOf(value.toString());
		}
		
		@Override
		public boolean isMessage(Object message) {
			return message instanceof Integer;
		}
	};
	
	@Override
	public M create() {
		return null;
	}
	
	@Override
	public M create(Message<?> parent) {
		return null;
	}

	@Override
	public <V> Property<M, V> property(Object key, Schema<V> type) {
		return new BaseProperty<M,V>(this, key, new NullActor<M,V>(type));
	}
}