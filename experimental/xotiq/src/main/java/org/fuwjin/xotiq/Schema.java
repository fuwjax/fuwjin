package org.fuwjin.xotiq;

public interface Schema<M> {
	M create();
	
	M create(Message<?> parent);
	
	M convert(Object value);
	
	boolean isMessage(Object message);
	
	Message<?> parentOf(M message);
	
	Property<M,?> property(Object key);

	<V> Property<M,V> property(Object key, Schema<V> type);

	Iterable<? extends Property<M, ?>> properties();
	
	Iterable<? extends Attribute<M, ?>> attributes(M message);
}
