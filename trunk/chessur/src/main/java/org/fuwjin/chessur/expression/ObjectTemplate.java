/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur.expression;

import java.util.ArrayList;
import java.util.List;
import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.Snapshot;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.Function;

/**
 * An expression representing a serialized object.
 */
public class ObjectTemplate implements Expression {
   private final List<FieldTemplate> setters = new ArrayList<FieldTemplate>();
   private final Function constructor;
   private final String type;

   /**
    * Creates a new instance.
    * @param type the object type
    * @param constructor the constructor function
    */
   public ObjectTemplate(final String type, final Function constructor) {
      this.type = type;
      this.constructor = constructor;
   }

   public Function constructor() {
      return constructor;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws ResolveException, AbortedException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      final Object object;
      try {
         object = constructor.invoke();
      } catch(final Exception e) {
         throw new ResolveException(e, "Could not construct object from template: %s: %s", type, snapshot);
      }
      for(final FieldTemplate field: setters) {
         try {
            final Object result = field.resolve(input, output, scope);
            field.invoke(object, result);
         } catch(final ResolveException e) {
            throw new ResolveException(e, "could not resolve value for %s: %s", field.name(), snapshot);
         } catch(final AbortedException e) {
            throw e;
         } catch(final Exception e) {
            throw new ResolveException(e, "could not inject value for %s: %s", field.name(), snapshot);
         }
      }
      return object;
   }

   /**
    * Adds a field to this template.
    * @param name the name of the field
    * @param setter the field setter
    * @param object the value
    */
   public void set(final String name, final Function setter, final Expression object) {
      setters.add(new FieldTemplate(name, setter, object));
   }

   public Iterable<FieldTemplate> setters() {
      return setters;
   }

   /**
    * Returns the type.
    * @return the type
    */
   public String type() {
      return type;
   }
}
