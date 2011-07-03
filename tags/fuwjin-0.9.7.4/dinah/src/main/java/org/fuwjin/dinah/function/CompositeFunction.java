/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.FunctionProvider.NoSuchFunctionException;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.dinah.signature.CompositeSignature;

/**
 * A container for Function composition. While it is presumed that all functions
 * will share the same name, this is not required.
 */
public class CompositeFunction extends AbstractFunction {
   public static AbstractFunction merge(final SignatureConstraint signature, final List<AbstractFunction> funcs)
         throws NoSuchFunctionException {
      if(funcs.size() == 1) {
         return funcs.get(0);
      }
      if(funcs.size() == 0) {
         throw new NoSuchFunctionException("No function matches %s", signature);
      }
      return new CompositeFunction(signature, funcs);
   }

   private static FunctionSignature[] signatures(final List<AbstractFunction> functions) {
      final FunctionSignature[] signatures = new FunctionSignature[functions.size()];
      int i = 0;
      for(final AbstractFunction function: functions) {
         signatures[i++] = function.signature();
      }
      return signatures;
   }

   private final List<AbstractFunction> functions;

   /**
    * Creates a new instance. The name of this function will be the name of the
    * first supplied function.
    * @param functions the set of functions, must be at least 1
    */
   private CompositeFunction(final SignatureConstraint constraint, final List<AbstractFunction> functions) {
      super(new CompositeSignature(constraint, signatures(functions)));
      this.functions = functions;
   }

   public AbstractFunction bestOption() {
      AbstractFunction best = null;
      for(final AbstractFunction function: functions) {
         if(!function.isPrivate() && best == null) {
            best = function;
         }
      }
      if(best == null) {
         return functions.get(0);
      }
      return best;
   }

   @Override
   public Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException {
      AdaptException failure = null;
      for(final AbstractFunction function: functions) {
         try {
            return function.invoke(args);
         } catch(final AdaptException e) {
            failure = e;
         }
      }
      if(failure == null) {
         throw new AdaptException("Impossible Exception: there must always be at least one function");
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
}
