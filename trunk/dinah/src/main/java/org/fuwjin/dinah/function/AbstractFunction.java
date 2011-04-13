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
import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;

public abstract class AbstractFunction implements Function {
   private final String name;
   private final Type[] argTypes;

   public AbstractFunction(final String name, final Type... argTypes) {
      this.name = name;
      this.argTypes = argTypes;
   }

   protected int argCount() {
      return argTypes.length;
   }

   protected Type argType(final int i) {
      return argTypes()[i];
   }

   protected Type[] argTypes() {
      return argTypes;
   }

   protected boolean isPrivate() {
      return member() == null || Modifier.isPrivate(member().getModifiers());
   }

   protected abstract Member member();

   @Override
   public final String name() {
      return name;
   }
}
