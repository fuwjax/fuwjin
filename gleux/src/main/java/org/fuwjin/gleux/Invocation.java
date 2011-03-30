package org.fuwjin.gleux;

import java.util.ArrayList;
import java.util.List;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionRenderer;
import org.fuwjin.postage.Postage;

/**
 * 
 */
public class Invocation implements Expression {
   private final String name;
   private Function function;
   private final List<Expression> params = new ArrayList<Expression>();

   /**
    * Creates a new instance.
    * @param name the postage function name
    */
   public Invocation(final String name) {
      this.name = name;
   }

   /**
    * Adds a new parameter value.
    * @param value the new value
    */
   public void addParam(final Expression value) {
      params.add(value);
   }

   public FunctionRenderer renderer() {
      return function.renderer(true);
   }

   /**
    * Resolves this invocation against a postage instance.
    * @param postage the postage instance
    */
   public void resolve(final Postage postage) {
      function = postage.getFunction(name);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder(name).append("(");
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
      final Object ret = function.invokeSafe(args);
      if(Postage.isSuccess(ret)) {
         return result.value(ret);
      }
      return state.failure(result.failure("Postage failure: %s", ret), "Could not invoke postage function");
   }
}
