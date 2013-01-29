package org.fuwjin.xotiq.impl;

public interface PropertyActor<M, V> {
	V get(M message);
	void set(M message, V value);
	void clear(M message);
	boolean has(M message);
	V convert(Object value);
}
