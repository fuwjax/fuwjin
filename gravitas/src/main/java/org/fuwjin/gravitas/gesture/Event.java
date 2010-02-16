/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.gravitas.gesture;

import java.util.concurrent.atomic.AtomicInteger;

public class Event{
   private static final AtomicInteger idGenerator = new AtomicInteger();
   private final Object gesture;
   private final Context source;
   private final int id;
   private final boolean broadcast;

   public Event(final Context source, final Object gesture, boolean broadcast){
      this.source = source;
      this.gesture = gesture;
      this.broadcast = broadcast;
      id = idGenerator.incrementAndGet();
   }

   public Object gesture(){
      return gesture;
   }

   public Context source(){
      return source;
   }
   
   public int id(){
      return id;
   }
   
   public boolean isBroadcast(){
      return broadcast;
   }
}