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
package org.fuwjin.postage.type;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExtendedWildcard implements WildcardType {
   private static final Type[] NO_BOUNDS = new Type[0];

   private static void merge(final List<Type> types, final Type merge) {
      final Iterator<Type> iter = types.iterator();
      while(iter.hasNext()) {
         final Type next = iter.next();
         if(TypeUtils.isAssignableTo(merge, next)) {
            return;
         }
         if(TypeUtils.isAssignableTo(next, merge)) {
            iter.remove();
         }
      }
      types.add(merge);
   }

   public static Type union(final Type type1, final Type type2) {
      final List<Type> types = new ArrayList<Type>(Arrays.asList(TypeUtils.getUpperBounds(type1)));
      for(final Type merge: TypeUtils.getUpperBounds(type2)) {
         merge(types, merge);
      }
      if(types.size() == 0) {
         return null;
      }
      if(types.size() == 1) {
         return types.get(0);
      }
      return new ExtendedWildcard(types.toArray(NO_BOUNDS));
   }

   private final Type[] upper;

   public ExtendedWildcard(final Type... upper) {
      this.upper = upper;
   }

   @Override
   public Type[] getLowerBounds() {
      return NO_BOUNDS;
   }

   @Override
   public Type[] getUpperBounds() {
      return upper;
   }
}
