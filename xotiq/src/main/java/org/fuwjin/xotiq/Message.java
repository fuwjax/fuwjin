package org.fuwjin.xotiq;


/**
 * Value object in the props project.
 * 
 * @author fuwjax
 * 
 */
public interface Message {
	/**
	 * The parent message for resolving missing attributes.
	 * 
	 * @return the parent message, never null
	 */
	Object parent();

	/**
	 * Returns the schema backing this message.
	 * 
	 * @return the schema, never null
	 */
	Schema schema();

	/**
	 * A convenience method for schema().property(key).attribute(this).
	 * @param key the property key
	 * @return the attribute for the key, never null
	 */
	Attribute attribute(Object key);
}
