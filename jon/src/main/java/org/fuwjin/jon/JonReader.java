/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import static org.fuwjin.io.AbstractCodePointStream.stream;
import static org.fuwjin.jon.BuilderRegistry.getBuilder;
import static org.fuwjin.pogo.PogoUtils.readGrammar;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.fuwjin.io.CodePointStream;
import org.fuwjin.io.PogoException;
import org.fuwjin.io.StreamPosition;
import org.fuwjin.jon.container.JonContainer;
import org.fuwjin.pogo.Grammar;

public class JonReader {
   class JonContext {
      public Object addReference(final Reference ref) {
         container().store(ref.name(), ref.value());
         return ref.value();
      }

      public Object getReference(final String key) {
         return container().get(key);
      }

      public Reference newReference(final Object value) {
         return new Reference(value);
      }
   }

   private static final Grammar JON;
   static {
      try {
         JON = readGrammar("jon.reader.pogo");
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final JonContext context;
   private final JonContainer container;
   private final CodePointStream stream;

   public JonReader(final CodePointStream stream) {
      this.stream = stream;
      context = new JonContext();
      container = new JonContainer();
   }

   public JonReader(final Reader reader) {
      this(stream(reader));
   }

   public JonReader(final String content) {
      this(new StringReader(content));
   }

   public JonContainer container() {
      return container;
   }

   public <T> T fill(final T obj) throws PogoException {
      final StreamPosition pos = new StreamPosition(stream);
      pos.reserve(null, context);
      return (T)JON.parse(pos, obj);
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
      try {
         final StreamPosition pos = new StreamPosition(stream);
         pos.reserve(null, context);
         while(true) {
            list.add((T)JON.parse(pos, getBuilder(type)));
         }
      } catch(final PogoException e) {
         // continue;
      }
      return list;
   }
}
