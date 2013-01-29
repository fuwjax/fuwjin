package org.fuwjin.xotiq;

public interface Property {
	Object key();

	Schema schema();

	/**
	 * Returns an attribute for the given message.
	 * @param message the message
	 * @return the attribute on the message
	 */
	Attribute attribute(Object message);
	
	Object value(Object message);
}
