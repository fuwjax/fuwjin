/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterable implements Iterable<String>{
   public static Iterable<String> lines(final BufferedReader reader){
      return new LineIterable(new BufferedReader(reader));
   }

   public static Iterable<String> lines(final InputStream stream){
      return lines(new InputStreamReader(stream));
   }

   public static Iterable<String> lines(final Reader reader){
      return lines(new BufferedReader(reader));
   }

   private final BufferedReader reader;

   private LineIterable(final BufferedReader reader){
      this.reader = reader;
   }

   @Override
   public Iterator<String> iterator(){
      return new Iterator<String>(){
         @Override
         public boolean hasNext(){
            try{
               return reader.ready();
            }catch(final IOException e){
               return false;
            }
         }

         @Override
         public String next(){
            try{
               return reader.readLine();
            }catch(final IOException e){
               final NoSuchElementException ex = new NoSuchElementException();
               ex.initCause(e);
               throw ex;
            }
         }

         @Override
         public void remove(){
            throw new UnsupportedOperationException();
         }
      };
   }
}