package org.fuwjin.xotiq.impl;

import static org.fuwjin.xotiq.impl.NullMessage.NULL_MESSAGE;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Message;
import org.fuwjin.xotiq.Property;

public class NullAttribute implements Attribute {
	private Property property;

	public NullAttribute(Property property) {
		this.property = property;
	}

	@Override
	public Message message() {
		return NULL_MESSAGE;
	}

	@Override
	public Property property() {
		return property;
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public Object set(Object value) {
		return null;
	}

	@Override
	public Object clear() {
		return null;
	}

	@Override
	public boolean has() {
		return false;
	}

}
