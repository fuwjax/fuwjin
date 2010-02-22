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
package org.fuwjin.util;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * An Iterable implementation that is backed by a fixed size
 * {@link AtomicReferenceArray}.
 * @param <T> the element type
 */
public class AtomicIterable<T> implements Iterable<T>{
   private final AtomicReferenceArray<T> array;

   /**
    * Creates a new instance.
    * @param length the length of the atomic array.
    */
   public AtomicIterable(final int length){
      assert length >= 0;
      array = new AtomicReferenceArray<T>(length);
   }

   @Override
   public AtomicIterator<T> iterator(){
      return new AtomicIterator<T>(array);
   }
}