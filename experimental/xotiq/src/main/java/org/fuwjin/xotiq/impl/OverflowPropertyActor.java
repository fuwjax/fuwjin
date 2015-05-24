package org.fuwjin.xotiq.impl;

import java.util.Map;

import org.fuwjin.xotiq.Schema;

public abstract class OverflowPropertyActor<M,K,V> implements PropertyActor<M,V> {
	private final K key;
	private Schema<V> schema;

	public OverflowPropertyActor(K key, Schema<V> valueType) {
		this.key = key;
		this.schema = valueType;
	}

	@Override
	public V get(M message) {
		return overflow(message).get(key);
	}

	@Override
	public void set(M message, V value) {
		overflow(message).put(key, value);
	}

	@Override
	public void clear(M message) {
		overflow(message).remove(key);
	}

	@Override
	public boolean has(M message) {
		return overflow(message).containsKey(key);
	}

	@Override
	public Schema<V> type() {
		return schema;
	}
	
	protected abstract Map<K,V> overflow(M message);
}