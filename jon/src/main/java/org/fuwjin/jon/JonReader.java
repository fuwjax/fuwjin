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
import org.fuwjin.pogo.PogoException;
import org.fuwjin.pogo.postage.PogoCategory;
import org.fuwjin.pogo.state.ParseState;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.ConstantCategory;
import org.fuwjin.postage.category.ReflectionCategory;

/**
 * Reads JON files.
 */
public class JonReader {
   private static final Grammar JON;
   private static final JonContainer container = new JonContainer();;
   static {
      try {
         final Postage postage = new Postage(new ReflectionCategory(), new PogoCategory(), new ConstantCategory().put(
               "context", container, JonContainer.class));
         JON = readGrammar(streamBytes("jon.reader.pogo"), postage);
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }
   private final CodePointStream stream;

   /**
    * Creates a new instance.
    * @param stream the input stream
    */
   public JonReader(final CodePointStream stream) {
      this.stream = stream;
   }

   /**
    * Creates a new instance.
    * @param reader the input reader
    */
   public JonReader(final Reader reader) {
      this(stream(reader));
   }

   /**
    * Creates a new instance.
    * @param content the literal JON string.
    */
   public JonReader(final String content) {
      this(streamOf(content));
   }

   /**
    * Fills an object from the stream.
    * @param <T> the object type
    * @param obj the object to fill
    * @return the filled object
    * @throws PogoException if the parse fails
    */
   public <T> T fill(final T obj) throws PogoException {
      container.clear();
      if(obj == null) {
         return (T)JON.parse(stream);
      }
      return (T)JON.parse(stream, obj);
   }

   /**
    * Reads the next object from the stream.
    * @return the next object
    * @throws PogoException if the parse fails
    */
   public Object read() throws PogoException {
      return read(null);
   }

   /**
    * Reads the next typed object from the stream.
    * @param <T> the type of the next object
    * @param type the object type
    * @return the object
    * @throws PogoException if the parse fails
    */
   public <T> T read(final Class<T> type) throws PogoException {
      return (T)fill(getBuilder(type));
   }

   /**
    * Reads all the objects from the stream.
    * @return the list
    */
   public List<Object> readAll() {
      return readAll(null);
   }

   /**
    * Reads all the objects from the stream.
    * @param <T> the element type
    * @param type the element type
    * @return the list of elements
    */
   public <T> List<T> readAll(final Class<T> type) {
      container.clear();
      final List<T> list = new LinkedList<T>();
      try {
         final PogoState pos = new ParseState(stream);
         while(true) {
            final Object value = JON.parse(pos, getBuilder(type));
            list.add((T)value);
         }
      } catch(final PogoException e) {
         // continue;
      }
      return list;
   }
}
