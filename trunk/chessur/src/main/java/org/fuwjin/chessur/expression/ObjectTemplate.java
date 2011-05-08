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

public class ObjectTemplate implements Expression {
   private static class FieldTemplate implements Expression {
      private final Function setter;
      private final Expression value;

      public FieldTemplate(final Function setter, final Expression value) {
         this.setter = setter;
         this.value = value;
      }

      public Object invoke(final Object object, final Object val) throws Exception {
         return setter.invoke(object, val);
      }

      @Override
      public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
            throws AbortedException, ResolveException {
         return value.resolve(input, output, scope);
      }

      String name() {
         return setter.name();
      }
   }

   private final List<FieldTemplate> setters = new ArrayList<FieldTemplate>();
   private final Function constructor;

   public ObjectTemplate(final Function constructor) {
      this.constructor = constructor;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws ResolveException, AbortedException {
      final Snapshot snapshot = new Snapshot(input, output, scope);
      final Object object;
      try {
         object = constructor.invoke();
      } catch(final Exception e) {
         throw new ResolveException(e, snapshot, "Could not construct object from template: %s", constructor.name());
      }
      for(final FieldTemplate field: setters) {
         try {
            final Object result = field.resolve(input, output, scope);
            field.invoke(object, result);
         } catch(final ResolveException e) {
            throw e.addStackTrace(snapshot, "could not resolve value for %s", field.name());
         } catch(final AbortedException e) {
            throw e;
         } catch(final Exception e) {
            throw new ResolveException(e, snapshot, "could not inject value for %s", field.name());
         }
      }
      return object;
   }

   public void set(final Function setter, final Expression object) {
      setters.add(new FieldTemplate(setter, object));
   }
}
