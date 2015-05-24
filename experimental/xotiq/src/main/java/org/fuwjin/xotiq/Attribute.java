package org.fuwjin.xotiq;

/**
 * The binding between a {@link Property} and a message. Note that the message
 * need not be an instance of {@link Message}. This binding reflects and mutates
 * the value of the property on the message at the time of the call, not the
 * time of creation. Attributes are not required to be thread-safe.
 * 
 * @author fuwjax
 * 
 */
public interface Attribute<M,V> {
	/**
	 * Returns the bound message.
	 * 
	 * @return the bound message
	 */
	M message();

	/**
	 * Returns the bound property.
	 * 
	 * @return the bound property
	 */
	Property<M,V> property();
	
	<O> Attribute<M,O> as(Schema<O> type);

	/**
	 * Returns the value of the property on the message.
	 * 
	 * @return the attribute value
	 */
	V get();

	/**
	 * Changes the value of the property to {@code value} and returns the
	 * previous value of the property on the message.
	 * 
	 * @param value
	 *            the new value, may be null
	 * @return the old attribute value, may be null
	 * @throws IllegalAccessException if the attribute is read only.
	 */
	V set(V value);

	/**
	 * Clears the value of the property on the message. Note that this is not
	 * equivalent to setting the value to null, as this method instead may
	 * expose any default or inherited value.
	 * 
	 * @return the old attribute value, may be null
	 * @throws IllegalAccessException if the attribute is read only.
	 */
	V clear();

	/**
	 * Returns true if the message currently has a set value for this property.
	 * Note that this has nothing to do with whether the value is null. A call
	 * to has() may return true when the corresponding get() would return null
	 * if the property has been explicitly set to null for the message. In
	 * addition, has() may return false and get() non-null if there is a default
	 * or inherited value for the attribute.
	 * 
	 * @return true if the attribute has a set value, false otherwise
	 */
	boolean has();
}
