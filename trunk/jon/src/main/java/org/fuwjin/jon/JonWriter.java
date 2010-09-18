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
import org.fuwjin.pogo.postage.PogoCategory;
import org.fuwjin.pogo.state.ParseException;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.ConstantCategory;
import org.fuwjin.postage.category.VoidCategory;

public class JonWriter {
   private static final Grammar JON;
   static {
      try {
         final Postage postage = new Postage(new PogoCategory(), new VoidCategory("default"), new ConstantCategory());
         JON = readGrammar(streamBytes("jon.writer.pogo"), postage);
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final ReferenceStorage storage;

   public JonWriter() {
      storage = new ReferenceStorage();
   }

   public String write(final Object obj) throws ParseException {
      return JON.toString(storage.get(obj, null));
   }

   public void write(final Object obj, final Appendable appender) throws ParseException {
      JON.serial(storage.get(obj, null), appender);
   }
}
