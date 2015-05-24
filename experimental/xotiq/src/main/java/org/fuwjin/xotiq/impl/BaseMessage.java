package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Message;

public abstract class BaseMessage<M extends Message<M>> extends AbstractMessage<M> {
	private Message<?> parent;

	protected BaseMessage() {
		parent = null;
	}

	public BaseMessage(Message<?> parent) {
		this.parent = parent;
	}

	@Override
	public final Message<?> parent() {
		return parent;
	}
}
