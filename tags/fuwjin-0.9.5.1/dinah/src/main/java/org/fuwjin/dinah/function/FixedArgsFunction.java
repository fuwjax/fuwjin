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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter;
import org.fuwjin.util.Adapter.AdaptException;

/**
 * Base class for methods, constructors and anything else with a fixed number of
 * required arguments.
 * @param <M> the reflection member type
 */
public abstract class FixedArgsFunction<M extends Member> extends AbstractFunction {
   private final M member;

   /**
    * Creates a new instance.
    * @param member the reflection member
    * @param name the function name
    * @param argTypes the set of required argument types
    */
   public FixedArgsFunction(final M member, final String name, final Type... argTypes) {
      super(name, argTypes);
      this.member = member;
   }

   @Override
   public Object invoke(final Object... args) throws AdaptException, InvocationTargetException {
      Adapter.adaptArray(args, argTypes());
      try {
         return invokeSafe(args);
      } catch(final IllegalAccessException e) {
         throw new AdaptException(e, "%s could not be accessed: %s", name(), e);
      } catch(final InstantiationException e) {
         throw new AdaptException(e, "%s could not be instantiated: %s", name(), e);
      } catch(final IllegalArgumentException e) {
         throw new AdaptException(e, "%s could not be adapted: %s", name(), e);
      }
   }

   @Override
   public AbstractFunction restrict(final FunctionSignature signature) {
      if(signature.matchesFixed(argTypes())) {
         return this;
      }
      return AbstractFunction.NULL;
   }

   @Override
   public String toString() {
      if(member == null) {
         return super.toString();
      }
      return getClass().getSimpleName() + ": " + member;
   }

   protected abstract Object invokeSafe(Object... args) throws AdaptException, InvocationTargetException,
         IllegalArgumentException, IllegalAccessException, InstantiationException;

   @Override
   protected boolean isPrivate() {
      return member() == null || Modifier.isPrivate(member().getModifiers());
   }

   protected final M member() {
      return member;
   }
}
