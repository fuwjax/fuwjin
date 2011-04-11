package org.fuwjin.gleux;

import java.util.ArrayList;
import java.util.List;

import org.fuwjin.dinah.Function;
import org.fuwjin.util.BusinessException;

/**
 * 
 */
public class Invocation implements Expression {
   private Function function;
   private final List<Expression> params = new ArrayList<Expression>();

   /**
    * Adds a new parameter value.
    * @param value the new value
    */
   public void addParam(final Expression value) {
      params.add(value);
   }

   public String name() {
      return function.name();
   }

   public int paramCount() {
      return params.size();
   }

   public void setFunction(final Function function) {
      this.function = function;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder(function.name()).append("(");
      boolean first = true;
      for(final Expression param: params) {
         if(first) {
            first = false;
         } else {
            builder.append(", ");
         }
         builder.append(param);
      }
      return builder.append(")").toString();
   }

   @Override
   public State transform(final State state) {
      final Object[] args = new Object[params.size()];
      int index = 0;
      State result = state;
      for(final Expression param: params) {
         result = param.transform(result);
         if(!result.isSuccess()) {
            return state.failure(result, "Could not resolve value %d", index);
         }
         args[index++] = result.value();
      }
      try {
         final Object ret = function.invoke(args);
         return result.value(ret);
      } catch(final Exception e) {
         if(e instanceof BusinessException) {
            System.out.println(e);
         }
         return state.failure(result.failure("Postage failure: %s", e), "Could not invoke postage function");
      }
   }
}
