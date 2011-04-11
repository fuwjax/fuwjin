package org.fuwjin.gleux;

import java.util.ArrayList;
import java.util.List;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.function.FunctionInvocationException;

public class ObjectTemplate implements Expression {
   private static class FieldTemplate {
      private final Function setter;
      private final Expression value;

      public FieldTemplate(final Function setter, final Expression value) {
         this.setter = setter;
         this.value = value;
      }
   }

   private final List<FieldTemplate> setters = new ArrayList<FieldTemplate>();
   private final Function constructor;

   public ObjectTemplate(final Function constructor) {
      this.constructor = constructor;
   }

   public void set(final Function setter, final Expression object) {
      setters.add(new FieldTemplate(setter, object));
   }

   @Override
   public State transform(final State state) {
      try {
         final Object object = constructor.invoke();
         State result = state;
         for(final FieldTemplate field: setters) {
            result = field.value.transform(result);
            if(!result.isSuccess()) {
               return state.failure(result.failure("Could not transform field value"),
                     "Could not build object from template");
            }
            final Object failure = field.setter.invoke(object, result.value());
            if(failure instanceof FunctionInvocationException) {
               return state
                     .failure(result.failure("Could not set field value"), "Could not build object from template");
            }
         }
         return state.value(object);
      } catch(final Exception e) {
         return state.failure("Could not construct object from template: %s", e);
      }
   }
}
