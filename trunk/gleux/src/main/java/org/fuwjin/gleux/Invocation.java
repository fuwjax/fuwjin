package org.fuwjin.gleux;

import java.util.ArrayList;
import java.util.List;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.function.FunctionInvocationException;

/**
 * 
 */
public class Invocation implements Expression {
   private final String name;
   private Function function;
   private final List<Expression> params = new ArrayList<Expression>();

   public Invocation(final Function function) {
      name = function.name();
      this.function = function;
   }

   /**
    * Adds a new parameter value.
    * @param value the new value
    */
   public void addParam(final Expression value) {
      params.add(value);
   }

   public void resolve() {
      function = function.restrict(new FunctionSignature(name, params.size()));
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
      final Object ret = function.invoke(args);
      if(ret instanceof FunctionInvocationException) {
         return state.failure(result.failure("Postage failure: %s", ret), "Could not invoke postage function");
      }
      return result.value(ret);
   }
}
