package org.fuwjin.xotiq;


public interface Schema {
	Object create();
	
	Object convert(Object value);

	Class<?> type();

	Schema parent();

	Property property(Object key);

	Iterable<? extends Property> properties();
}
