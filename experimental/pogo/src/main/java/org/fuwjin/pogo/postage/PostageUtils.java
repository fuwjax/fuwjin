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
package org.fuwjin.pogo.postage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.FunctionTarget;
import org.fuwjin.postage.TargetTransform;

/**
 * A bridge class for using legacy Pogo with the new Postage library. This class
 * should be refactored away.
 */
public class PostageUtils {
   public enum Return {
      RETURN;
   }

   private static final class ReturnTarget implements FunctionTarget {
      private final FunctionTarget target;

      private ReturnTarget(final FunctionTarget target) {
         this.target = target;
      }

      @Override
      public boolean equals(final Object obj) {
         try {
            final ReturnTarget o = (ReturnTarget)obj;
            return getClass().equals(o.getClass()) && target.equals(o.target);
         } catch(final RuntimeException e) {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return target.hashCode();
      }

      @Override
      public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
         final Object result = target.invoke(args);
         if(result instanceof Failure) {
            return result;
         }
         return args[args.length - 1];
      }

      @Override
      public Type parameterType(final int index) {
         return target.parameterType(index);
      }
@Override
public String render(boolean castArgs, String... argRepresentations){
	return "return "+target.render(castArgs, argRepresentations);
}

      @Override
      public int requiredArguments() {
         return target.requiredArguments();
      }

      @Override
      public Type returnType() {
         return parameterType(requiredArguments() - 1);
      }
   }

   private static final class ThisTarget implements FunctionTarget {
      private final FunctionTarget target;
      private final boolean handleFalse;

      private ThisTarget(final FunctionTarget target, final boolean handleFalse) {
         this.target = target;
         this.handleFalse = handleFalse;
      }

      @Override
      public boolean equals(final Object obj) {
         try {
            final ThisTarget o = (ThisTarget)obj;
            return getClass().equals(o.getClass()) && target.equals(o.target) && handleFalse == o.handleFalse;
         } catch(final RuntimeException e) {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return target.hashCode();
      }

      @Override
      public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
         final Object result = target.invoke(args);
         if(result instanceof Failure) {
            return result;
         }
         if(handleFalse && result instanceof Boolean && !(Boolean)result) {
            return new Failure("function returned false");
         }
         return args[0];
      }

 	@Override
	public String render(boolean castArgs, String... argRepresentations){
		return argRepresentations[0];
	}
     @Override
      public Type parameterType(final int index) {
         return target.parameterType(index);
      }

      @Override
      public int requiredArguments() {
         return target.requiredArguments();
      }

      @Override
      public Type returnType() {
         return parameterType(0);
      }
   }

   public static final TargetTransform POGO_FILTER = new TargetTransform() {
      @Override
      public FunctionTarget transform(final FunctionTarget target) {
         if(Return.class.equals(target.returnType())) {
            return new ReturnTarget(target);
         }
         if(void.class.equals(target.returnType()) || boolean.class.equals(target.returnType())) {
            return new ThisTarget(target, boolean.class.equals(target.returnType()));
         }
         return target;
      }
   };

   /**
    * Returns true if category is not default.
    * @param category the category to test
    * @return false if the category is default, true otherwise
    */
   public static boolean isCustomCategory(final String category) {
      return !"default".equals(category);
   }
}
