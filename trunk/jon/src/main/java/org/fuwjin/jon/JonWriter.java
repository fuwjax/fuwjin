/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import static org.fuwjin.pogo.CodePointStreamFactory.streamBytes;
import static org.fuwjin.pogo.PogoGrammar.readGrammar;

import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.postage.PogoCategory;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.ConstantCategory;
import org.fuwjin.postage.category.ReflectionCategory;

/**
 * Manages writing for JON files.
 */
public class JonWriter {
   private static final Grammar JON;
   static {
      try {
         final Postage postage = new Postage(new ReflectionCategory(), new PogoCategory(), new ConstantCategory());
         JON = readGrammar(streamBytes("jon.writer.pogo"), postage);
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final ReferenceStorage storage;

   /**
    * Creates a new instance.
    */
   public JonWriter() {
      storage = new ReferenceStorage();
   }

   /**
    * Writes the object to a string in a JON format.
    * @param obj the object to serialize
    * @return the JON formatted string
    * @throws PogoException if the serialize fails
    */
   public String write(final Object obj) throws PogoException {
      return JON.toString(storage.get(obj, null));
   }

   /**
    * Writes the object to the appender in a JON format.
    * @param obj the object to serialize
    * @param appender the destination for the serialization
    * @throws PogoException if the serialize fails
    */
   public void write(final Object obj, final Appendable appender) throws PogoException {
      JON.serial(storage.get(obj, null), appender);
   }
}
