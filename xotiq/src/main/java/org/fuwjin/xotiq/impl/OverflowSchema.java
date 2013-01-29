package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Schema;

public abstract class OverflowSchema<M extends OverflowMessage> extends MessageSchema<M>{
	protected OverflowSchema(Schema parent){
		super(parent);
	}
	
	protected OverflowSchema(){
		// no parent
	}

	@Override
	protected PropertyActor<M, Object> createActor(final Object key) {
		return new PropertyActor<M, Object>(){
			@Override
			public Object get(M message) {
				return message.overflow().get(key);
			}

			@Override
			public void set(M message, Object value) {
				message.overflow().put(key, value);
			}

			@Override
			public void clear(M message) {
				message.overflow().remove(key);
			}

			@Override
			public boolean has(M message) {
				return message.overflow().containsKey(key);
			}

			@Override
			public Object convert(Object value) {
				return value;
			}
		};
	}
}
