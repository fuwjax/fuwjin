package org.fuwjin.xotiq;

public interface Property<M,V> {
	/**
	 * Returns the key.
	 * @return the key
	 */
	Object key();

	/**
	 * Returns the schema.
	 * @return the schema
	 */
	Schema<M> schema();

	/**
	 * Returns an attribute for the given message.
	 * @param message the message
	 * @return the attribute on the message
	 */
	Attribute<M,V> attribute(M message);
	
	<O> Attribute<M,O> attribute(M message, Schema<O> type);
	
	<O> Property<M,O> as(Schema<O> type);
	
	V get(M message);
	
	V set(M message, V value);
	
	V clear(M message);
	
	boolean has(M message);

	Schema<V> value();
}
