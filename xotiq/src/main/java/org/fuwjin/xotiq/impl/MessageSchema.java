package org.fuwjin.xotiq.impl;

import static org.fuwjin.xotiq.impl.NullSchema.NULL_SCHEMA;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Schema;

public abstract class MessageSchema<M extends Message> extends BaseSchema<M>{
	private Schema parent;

	protected MessageSchema(Schema parent){
		this.parent = parent;
	}
	
	protected MessageSchema(){
		this(NULL_SCHEMA);
	}
	
	@Override
	public Schema parent() {
		return parent;
	}
	
	@Override
	protected Object parentOf(M message) {
		return message.parent();
	}
	
	@Override
	protected PropertyActor<M,Object> createActor(final Object key) {
		return new PropertyActor<M,Object>(){
			@Override
			public Object get(M message) {
				return null;
			}

			@Override
			public void set(M message, Object value) {
				throw new IllegalArgumentException("No such property: "+type()+"."+key);
			}

			@Override
			public void clear(M message) {
				// do nothing
			}

			@Override
			public boolean has(M message) {
				return false;
			}
			
			@Override
			public Object convert(Object value) {
				return value;
			}
		};
	}
}
