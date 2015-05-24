package org.fuwjin.xotiq.impl;

import org.fuwjin.xotiq.Schema;

public interface PropertyActor<M, V> {
	V get(M message);
	void set(M message, V value);
	boolean has(M message);
	void clear(M message);
	Schema<V> type();
}
