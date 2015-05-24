package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Message;

public abstract class BaseMessageSchema<M extends Message<M>> extends BaseSchema<M>{
	@Override
	public final Message<?> parentOf(M message) {
		return message.parent();
	}
	
	@Override
	public abstract M create(Message<?> parent);
}
