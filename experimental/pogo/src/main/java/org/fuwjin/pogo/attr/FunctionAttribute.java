package org.fuwjin.pogo.attr;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.type.Optional;

/**
 * An attribute whose value is computed by a Postage function.
 */
public class FunctionAttribute implements Attribute {
   private final String name;
   private final List<Attribute> params = new LinkedList<Attribute>();
   private Function function;

   /**
    * Creates a new instance.
    * @param name the full name of a Postage function
    */
   public FunctionAttribute(final String name) {
      this.name = name;
   }

   /**
    * Appends an attribute as a parameter to the list of parameters.
    * @param param the next parameter
    */
   public void addParameter(final Attribute param) {
      params.add(param);
   }

   @Override
   public void prepare(final State state, final Map<String, Object> scope) {
      for(final Attribute param: params) {
         param.prepare(state, scope);
      }
   }

   /**
    * Resolves this attribute against the postage container.
    * @param postage the postage container
    */
   public void resolve(final Postage postage) {
      final Type[] types = new Type[params.size()];
      int i = 0;
      for(final Attribute param: params) {
         types[i++] = param.type();
      }
      function = postage.getFunction(name).withSignature(Optional.OBJECT, types);
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      final Object result = value(state, scope);
      if(Postage.isSuccess(result)) {
         return state;
      }
      return state.failure(result, "Could not invoke %s", name);
   }

   @Override
   public Type type() {
      return function.returnType();
   }

   @Override
   public Object value(final State state, final Map<String, Object> scope) {
      final Object[] args = new Object[params.size()];
      int i = 0;
      for(final Attribute param: params) {
         final Object result = param.value(state, scope);
         if(Postage.isSuccess(result)) {
            args[i++] = result;
         } else {
            return result;
         }
      }
      return function.invokeSafe(args);
   }
}
