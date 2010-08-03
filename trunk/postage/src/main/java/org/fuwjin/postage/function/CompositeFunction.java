package org.fuwjin.postage.function;

import java.util.ArrayList;
import java.util.List;

import org.fuwjin.postage.CompositeFailure;
import org.fuwjin.postage.CompositeSignature;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;

public class CompositeFunction extends AbstractFunction {
   private final List<Function> functions = new ArrayList<Function>();

   public CompositeFunction(final String name) {
      super(new CompositeSignature(name));
   }

   public void addFunction(final Function function) {
      functions.add(function);
      signature().add(function.signature());
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CompositeFunction o = (CompositeFunction)obj;
         return super.equals(obj) && functions.equals(o.functions);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invokeSafe(final Object... args) {
      final CompositeFailure failure = new CompositeFailure("No valid function %s found for %s", this, args);
      for(final Function function: functions) {
         final Object result = function.invokeSafe(args);
         if(result instanceof Failure) {
            failure.addFailure((Failure)result);
         } else {
            return result;
         }
      }
      return failure;
   }

   @Override
   public CompositeSignature signature() {
      return (CompositeSignature)super.signature();
   }

   @Override
   public String toString() {
      return functions.toString();
   }
}
