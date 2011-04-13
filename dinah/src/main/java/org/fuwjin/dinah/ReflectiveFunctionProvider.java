/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.dinah.function.FailFunction;

public class ReflectiveFunctionProvider implements FunctionProvider {
   private final Map<String, FunctionProvider> providers = new HashMap<String, FunctionProvider>();

   @Override
   public Function getFunction(final FunctionSignature signature) {
      try {
         return getProvider(signature.name()).getFunction(signature);
      } catch(final Exception e) {
         return new FailFunction(signature.name(), e, "Could not find function %s", signature);
      }
   }

   protected FunctionProvider getProvider(final String name) throws Exception {
      final String typeName = getTypeName(name);
      FunctionProvider provider = providers.get(typeName);
      if(provider == null) {
         provider = new ReflectiveTypeFunctionProvider(typeName);
         providers.put(typeName, provider);
      }
      return provider;
   }

   protected String getTypeName(final String name) {
      final int index = name.lastIndexOf('.');
      if(index < 0) {
         throw new IllegalArgumentException(name + " is not a valid function name");
      }
      return name.substring(0, index);
   }
}
