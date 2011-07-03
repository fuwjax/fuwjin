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
import java.lang.reflect.Modifier;
import org.fuwjin.dinah.FunctionSignature;

/**
 * Base class for methods, constructors and anything else with a fixed number of
 * required arguments.
 * @param <M> the reflection member type
 */
public abstract class MemberFunction<M extends Member> extends AbstractFunction {
   private final M member;

   /**
    * Creates a new instance.
    * @param adapter the type converter
    * @param member the reflection member
    * @param name the function name
    * @param argTypes the set of required argument types
    */
   public MemberFunction(final M member, final FunctionSignature signature) {
      super(signature);
      this.member = member;
   }

   /**
    * Returns the reflective member.
    * @return the member, or null if there is none
    */
   public final M member() {
      return member;
   }

   @Override
   public String toString() {
      if(member == null) {
         return super.toString();
      }
      return getClass().getSimpleName() + ": " + member;
   }

   @Override
   protected boolean isPrivate() {
      return member() == null || Modifier.isPrivate(member().getModifiers());
   }
}
