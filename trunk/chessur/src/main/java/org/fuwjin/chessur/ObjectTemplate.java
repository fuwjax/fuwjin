/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur;

import java.util.ArrayList;
import java.util.List;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.function.FunctionInvocationException;

public class ObjectTemplate implements Expression{
   private static class FieldTemplate{
      private final Function setter;
      private final Expression value;

      public FieldTemplate(final Function setter, final Expression value){
         this.setter = setter;
         this.value = value;
      }

      public Object invoke(final Object object, final Object val) throws Exception{
         return setter.invoke(object, val);
      }

      public State transform(final State result){
         return value.transform(result);
      }
   }

   private final List<FieldTemplate> setters = new ArrayList<FieldTemplate>();
   private final Function constructor;

   public ObjectTemplate(final Function constructor){
      this.constructor = constructor;
   }

   public void set(final Function setter, final Expression object){
      setters.add(new FieldTemplate(setter, object));
   }

   @Override
   public State transform(final State state){
      try{
         final Object object = constructor.invoke();
         State result = state;
         for(final FieldTemplate field: setters){
            result = field.transform(result);
            if(!result.isSuccess()){
               return state.failure(result.failure("Could not transform field value"),
                     "Could not build object from template");
            }
            final Object failure = field.invoke(object, result.value());
            if(failure instanceof FunctionInvocationException){
               return state
                     .failure(result.failure("Could not set field value"), "Could not build object from template");
            }
         }
         return state.value(object);
      }catch(final Exception e){
         return state.failure("Could not construct object from template: %s", e);
      }
   }
}
