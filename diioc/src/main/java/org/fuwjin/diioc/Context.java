package org.fuwjin.diioc;

public interface Context {
	<T> Binding<T> bind(Key<T> key) throws Exception;
}
