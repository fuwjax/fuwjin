package org.fuwjin.diioc;


public interface Context {
	<T> T create(Diioc root, Key<T> key) throws Exception;
}
