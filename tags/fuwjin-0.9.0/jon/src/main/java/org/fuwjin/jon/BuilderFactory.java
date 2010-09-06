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

public class BuilderFactory {
   private Class<?> type;
   private transient Builder builder;

   public Builder forType() {
      builder = getBuilder(type);
      return builder;
   }

   public Object toObject() {
      if(builder == null) {
         throw new RuntimeException();
      }
      return builder.toObject();
   }
}
