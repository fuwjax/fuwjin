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
package org.fuwjin.jon.container;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.jon.Reference;

/**
 * Manages references for a JON operation.
 */
public class JonContainer {
   private final ThreadLocal<Map<String, Object>> references = new ThreadLocal<Map<String, Object>>() {
      @Override
      protected java.util.Map<String, Object> initialValue() {
         return new HashMap<String, Object>();
      }
   };

   /**
    * Adds a new reference.
    * @param ref the reference
    * @return the reference value
    */
   public Object addReference(final Reference ref) {
      store(ref.name(), ref.value());
      return ref.value();
   }

   /**
    * Clears the references.
    */
   public void clear() {
      references().clear();
   }

   /**
    * Gets a reference value by name.
    * @param name the name of the reference
    * @return the reference value
    */
   public Object get(final String name) {
      Object ref = references().get(name);
      if(ref == null) {
         ref = new ContainerProxy();
         references().put(name, ref);
      }
      return ref;
   }

   /**
    * Gets a reference value by name.
    * @param key the name of the reference
    * @return the reference value
    */
   public Object getReference(final String key) {
      return get(key);
   }

   /**
    * Creates a new reference.
    * @return the new reference
    */
   public Reference newReference() {
      return new Reference();
   }

   /**
    * Creates a new reference for the given value.
    * @param value the reference value
    * @return the new reference
    */
   public Reference newReference(final Object value) {
      return new Reference(value);
   }

   private Map<String, Object> references() {
      return references.get();
   }

   /**
    * Stores a name/value pair, resolving any forward references.
    * @param name the reference name
    * @param value the reference value
    */
   public void store(final String name, final Object value) {
      final Object forward = references().put(name, value);
      if(forward != null && !forward.equals(value)) {
         ((ContainerProxy)forward).resolve(value);
      }
   }
}
