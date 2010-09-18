/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import static org.fuwjin.jon.BuilderRegistry.getBuilder;
import static org.fuwjin.pogo.CodePointStreamFactory.stream;
import static org.fuwjin.pogo.CodePointStreamFactory.streamBytes;
import static org.fuwjin.pogo.CodePointStreamFactory.streamOf;
import static org.fuwjin.pogo.PogoGrammar.readGrammar;

import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.fuwjin.jon.container.JonContainer;
import org.fuwjin.pogo.CodePointStream;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.postage.PogoCategory;
import org.fuwjin.pogo.state.ParseException;
import org.fuwjin.pogo.state.ParseState;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.InstanceCategory;
import org.fuwjin.postage.category.VoidCategory;

public class JonReader {
   private static final Grammar JON;
   private static final JonContainer container = new JonContainer();;
   static {
      try {
         final Postage postage = new Postage(new PogoCategory(), new VoidCategory("default"), new InstanceCategory(
               "context", container));
         JON = readGrammar(streamBytes("jon.reader.pogo"), postage);
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final CodePointStream stream;

   public JonReader(final CodePointStream stream) {
      this.stream = stream;
   }

   public JonReader(final Reader reader) {
      this(stream(reader));
   }

   public JonReader(final String content) {
      this(streamOf(content));
   }

   public <T> T fill(final T obj) throws ParseException {
      container.clear();
      if(obj == null) {
         return (T)JON.parse(stream);
      }
      return (T)JON.parse(stream, obj);
   }

   public Object read() throws ParseException {
      return read(null);
   }

   public <T> T read(final Class<T> type) throws ParseException {
      return (T)fill(getBuilder(type));
   }

   public List<Object> readAll() {
      return readAll(null);
   }

   public <T> List<T> readAll(final Class<T> type) {
      container.clear();
      final List<T> list = new LinkedList<T>();
      try {
         final PogoState pos = new ParseState(stream);
         while(true) {
            final Object value = JON.parse(pos, getBuilder(type));
            list.add((T)value);
         }
      } catch(final ParseException e) {
         // continue;
      }
      return list;
   }
}
