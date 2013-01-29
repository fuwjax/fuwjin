package org.fuwjin.xotiq.impl;

import static org.fuwjin.xotiq.impl.NullSchema.NULL_SCHEMA;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Schema;

public class NullMessage implements Message {
	public static final Message NULL_MESSAGE = new NullMessage();
	
	protected NullMessage() {
		// reduced visibility to discourage instantiation
	}

	@Override
	public Message parent() {
		return this;
	}

	@Override
	public Schema schema() {
		return NULL_SCHEMA;
	}

	@Override
	public Attribute attribute(Object key) {
		return new NullAttribute(new NullProperty(key));
	}
}
