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
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.FunctionSignature;

/**
 * Base class for methods, constructors and anything else with a fixed number of
 * required arguments.
 * @param <M> the reflection member type
 */
public abstract class FixedArgsFunction<M extends Member> extends AbstractFunction {
   private final M member;
   private final Adapter adapter;

   /**
    * Creates a new instance.
    * @param member the reflection member
    * @param name the function name
    * @param argTypes the set of required argument types
    */
   public FixedArgsFunction(final Adapter adapter, final M member, final String name, final Type... argTypes) {
      super(name, argTypes);
      this.adapter = adapter;
      this.member = member;
   }

   @Override
   public Object invoke(final Object... args) throws AdaptException, InvocationTargetException {
      adaptArray(args, argTypes());
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

   public final M member() {
      return member;
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

   /**
    * Adapts the array in place, adapting each element to the corresponding
    * element in types.
    * @param array the values to adapt
    * @param types the target types
    * @throws AdaptException if the array elements cannot be adapted, or the
    *         arrays are not the same size
    */
   protected void adaptArray(final Object[] array, final Type[] types) throws AdaptException {
      if(array.length != types.length) {
         throw new AdaptException("expected %d args not %d", types.length, array.length);
      }
      for(int i = 0; i < array.length; ++i) {
         array[i] = adapter.adapt(array[i], types[i]);
      }
   }

   protected abstract Object invokeSafe(Object... args) throws AdaptException, InvocationTargetException,
         IllegalArgumentException, IllegalAccessException, InstantiationException;

   @Override
   protected boolean isPrivate() {
      return member() == null || Modifier.isPrivate(member().getModifiers());
   }
}
