/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import static org.fuwjin.jon.BuilderRegistry.getBuilder;

import org.fuwjin.jon.builder.Builder;

/**
 * Builds new builder instances.
 */
public class BuilderFactory {
   private Class<?> type;
   private transient Builder builder;

   /**
    * Creates a builder for the internal type.
    * @return the builder
    */
   public Builder forType() {
      builder = getBuilder(type);
      return builder;
   }

   /**
    * Creates a new object from the internal builder.
    * @return the new object
    */
   public Object toObject() {
      if(builder == null) {
         throw new RuntimeException();
      }
      return builder.toObject();
   }
}
