package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Schema;

public class MessageWrapper<M> extends BaseMessage<MessageWrapper<M>> {
	private M wrapped;
	private MessageWrapperSchema<M> schema;

	public MessageWrapper(MessageWrapperSchema<M> schema, M wrapped) {
		this.schema = schema;
		this.wrapped = wrapped;
	}

	public MessageWrapper(MessageWrapperSchema<M> schema, M wrapped, Message<?> parent) {
		super(parent);
		this.schema = schema;
		this.wrapped = wrapped;
	}
	
	@Override
	public Schema<MessageWrapper<M>> schema() {
		return schema;
	}

	protected M wrapped() {
		return wrapped;
	}
}
