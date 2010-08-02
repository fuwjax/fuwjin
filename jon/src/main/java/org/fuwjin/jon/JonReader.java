/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import static org.fuwjin.io.BufferedInput.buffer;
import static org.fuwjin.jon.BuilderRegistry.getBuilder;
import static org.fuwjin.pogo.PogoUtils.readGrammar;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.fuwjin.io.PogoException;
import org.fuwjin.jon.container.JonContainer;
import org.fuwjin.pogo.Grammar;

public class JonReader {
   private static final Grammar JON;
   static {
      try {
         JON = readGrammar("jon.reader.pogo");
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final JonReadContext context;

   public JonReader(final CharSequence seq) {
      context = new JonReadContext(seq);
   }

   public JonReader(final Reader reader) {
      this(buffer(reader));
   }

   public JonContainer container() {
      return context.container();
   }

   public <T> T fill(final T obj) throws PogoException {
      return (T)JON.parse(context, obj).get();
   }

   public Object read() throws PogoException {
      return read(null);
   }

   public <T> T read(final Class<T> type) throws PogoException {
      return (T)fill(getBuilder(type));
   }

   public List<Object> readAll() throws PogoException {
      return readAll(null);
   }

   public <T> List<T> readAll(final Class<T> type) throws PogoException {
      final List<T> list = new LinkedList<T>();
      while(context.hasRemaining()) {
         list.add(read(type));
      }
      return list;
   }
}
