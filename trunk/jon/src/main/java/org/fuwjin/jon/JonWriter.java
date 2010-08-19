/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import static org.fuwjin.pogo.PogoUtils.readGrammar;

import org.fuwjin.io.PogoException;
import org.fuwjin.io.SerialStreamPosition;
import org.fuwjin.jon.ref.ReferenceStorage;
import org.fuwjin.pogo.Grammar;

public class JonWriter {
   private static final Grammar JON;
   static {
      try {
         JON = readGrammar("jon.writer.pogo");
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final ReferenceStorage storage;

   public JonWriter() {
      storage = new ReferenceStorage();
   }

   public String write(final Object obj) throws PogoException {
      final StringBuilder builder = new StringBuilder();
      final SerialStreamPosition context = new SerialStreamPosition(builder);
      JON.parse(context, storage.get(obj, null));
      return builder.toString();
   }
}
