package org.fuwjin.xotiq;

public interface Message<M extends Message<M>> {
	Object get(Object key);
	
	Message<?> parent();
	
	Schema<M> schema();
	
	Attribute<M,?> attribute(Object key);
	
	<V> Attribute<M,V> attribute(Object key, Schema<V> type);
	
	Iterable<? extends Attribute<M,?>> attributes();
}
