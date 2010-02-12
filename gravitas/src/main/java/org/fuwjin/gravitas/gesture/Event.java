/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjin.gravitas.gesture;

public class Event{
   private final Integration source;
   private final Object gesture;

   public Event(Integration source, Object gesture){
      this.source = source;
      this.gesture = gesture;
   }
   
   public Integration source(){
      return source;
   }

   public Object gesture(){
      return gesture;
   }
}