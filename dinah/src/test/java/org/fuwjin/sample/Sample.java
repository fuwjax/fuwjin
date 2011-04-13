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
package org.fuwjin.sample;

import org.fuwjin.util.ObjectUtils;
import org.fuwjin.util.ObjectUtils.ObjectHelper;

public class Sample implements ObjectHelper{
   public static String staticValue;

   public static String doStatic(final String value){
      return staticValue + ":" + value;
   }

   private final String value;

   public Sample(final String value){
      this.value = value;
   }

   @Override
   public boolean equals(final Object obj){
      return ObjectUtils.isEqual(this, obj);
   }

   public String getValue(){
      return "get:" + value;
   }

   @Override
   public int hashCode(){
      return ObjectUtils.hash(this);
   }

   @Override
   public Object[] identity(){
      return new Object[]{value};
   }

   @Override
   public String toString(){
      return ObjectUtils.toString(this);
   }
}
