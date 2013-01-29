package org.fuwjin.xotiq.impl;

import static org.fuwjin.xotiq.impl.NullSchema.NULL_SCHEMA;

import org.fuwjin.xotiq.Attribute;
import org.fuwjin.xotiq.Property;
import org.fuwjin.xotiq.Schema;

public class NullProperty implements Property {
	private Object key;

	public NullProperty(Object key) {
		this.key = key;
	}

	@Override
	public Object key() {
		return key;
	}

	@Override
	public Schema schema() {
		return NULL_SCHEMA;
	}

	@Override
	public Attribute attribute(Object message) {
		return new NullAttribute(this);
	}

	@Override
	public Object value(Object message) {
		return null;
	}

}
