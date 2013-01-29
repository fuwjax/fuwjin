package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Schema;


public interface TypedSchema<M> extends Schema {
	@Override
	public M convert(Object value);
	
	@Override
	public M create();
	
	@Override
	public Class<M> type();
}
