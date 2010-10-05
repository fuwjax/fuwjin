/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjax.sample;

import java.util.HashMap;
import java.util.Map;

import org.fuwjax.sample.Phone.PhoneType;

/**
 * A hypothetical model for the Sample
 */
public class Model{
	private String name;
	private String description;
	private Map<PhoneType, Phone> contactNumbers;

	/**
	 * Creates a new instance.
	 * @param name the name of the model
	 */
	public Model(String name){
		this.name = name;
		contactNumbers = new HashMap<PhoneType, Phone>();
	}
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the description.
	 * @return the description
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Sets the description.
	 * @param description the new description
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/**
	 * Returns the contact phone for <code>type</code>.
	 * @param type the type of phone contact
	 * @return the phone number for the contact type
	 */
	public Phone getContact(PhoneType type){
		return contactNumbers.get(type);
	}
	
	/**
	 * Adds a new contact.
	 * @param type the contact type
	 * @param phone the contact phone
	 */
	public void addContact(PhoneType type, Phone phone){
		contactNumbers.put(type, phone);
	}
}
