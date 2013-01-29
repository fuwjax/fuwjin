package org.fuwjin.xotiq.impl;

import java.util.Collections;

import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public class NullSchema implements Schema{
	public static final Schema NULL_SCHEMA = new NullSchema();
	
	protected NullSchema(){
		// reduced visibility to discourage instantiation
	}
	
	@Override
	public Message create() {
		return NullMessage.NULL_MESSAGE;
	}
	
	@Override
	public Object convert(Object value) {
		return null;
	}

	@Override
	public Class<?> type() {
		return NullMessage.class;
	}

	@Override
	public Schema parent() {
		return this;
	}

	@Override
	public Property property(Object key) {
		return new NullProperty(key);
	}

	@Override
	public Iterable<? extends Property> properties() {
		return Collections.emptySet();
	}
}
