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
package org.fuwjin.dinah.function;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.BusinessException;
import org.fuwjin.util.CompositeException;

public class CompositeFunction extends AbstractFunction {
   private final List<AbstractFunction> functions;

   public CompositeFunction(final AbstractFunction... functions) {
      super(functions[0].name());
      this.functions = new ArrayList(Arrays.asList(functions));
   }

   private CompositeFunction(final List<AbstractFunction> functions) {
      super(functions.get(0).name());
      this.functions = functions;
   }

   public void add(final AbstractFunction function) {
      functions.add(function);
   }

   @Override
   public Object invoke(final Object... args) throws Exception {
      Exception failure = null;
      for(final AbstractFunction function: functions) {
         try {
            return function.invoke(args);
         } catch(final BusinessException e) {
            failure = CompositeException.compose(failure, e);
         }
      }
      if(failure == null) {
         throw new FunctionInvocationException("No functions exist for %s", name());
      }
      throw failure;
   }

   @Override
   public boolean isPrivate() {
      for(final AbstractFunction function: functions) {
         if(!function.isPrivate()) {
            return false;
         }
      }
      return true;
   }

   @Override
   protected Member member() {
      return null;
   }

   @Override
   public Function restrict(final FunctionSignature signature) {
      final List<AbstractFunction> restricted = new ArrayList<AbstractFunction>();
      for(final AbstractFunction function: functions) {
         final AbstractFunction func = (AbstractFunction)function.restrict(signature);
         if(!(func instanceof FailFunction)) {
            restricted.add(func);
         }
      }
      if(restricted.size() > 1) {
         Function result = null;
         for(final AbstractFunction function: restricted) {
            if(!function.isPrivate()) {
               if(result != null) {
                  result = null;
                  break;
               }
               result = function;
            }
         }
         if(result != null) {
            return result;
         }
      }
      if(restricted.size() == 1) {
         return restricted.get(0);
      }
      if(restricted.size() == 0) {
         return new FailFunction(name(), "Arg mismatch");
      }
      return new CompositeFunction(restricted);
   }
}
