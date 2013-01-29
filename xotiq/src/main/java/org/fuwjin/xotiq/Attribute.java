package org.fuwjin.xotiq;

public interface Attribute {
	Object message();

	Property property();

	Object get();

	Object set(Object value);

	Object clear();

	boolean has();
}
