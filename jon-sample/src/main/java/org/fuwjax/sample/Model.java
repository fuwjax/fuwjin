/*
 * This file is part of JON.
 *
 * JON is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * JON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2007 Michael Doberenz
 */
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
