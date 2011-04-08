package org.fuwjin.dinah.function;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;

public class CompositeFunction extends AbstractFunction {
   private final List<AbstractFunction> functions;

   public CompositeFunction(final AbstractFunction... functions) {
      super(functions[0].name());
      this.functions = new ArrayList(Arrays.asList(functions));
   }

   private CompositeFunction(final List<AbstractFunction> functions) {
      super(functions.get(0).name());
      this.functions = functions;
   }

   public void add(final AbstractFunction function) {
      functions.add(function);
   }

   @Override
   protected void invokeWithResult(final InvokeResult result, final Object[] args) {
      for(final AbstractFunction function: functions) {
         function.invokeWithResult(result, args);
         if(result.isComplete()) {
            return;
         }
         result.reset();
      }
   }

   @Override
   public boolean isPrivate() {
      for(final AbstractFunction function: functions) {
         if(!function.isPrivate()) {
            return false;
         }
      }
      return true;
   }

   @Override
   protected Member member() {
      return null;
   }

   @Override
   public Function restrict(final FunctionSignature signature) {
      final List<AbstractFunction> restricted = new ArrayList<AbstractFunction>();
      for(final AbstractFunction function: functions) {
         final AbstractFunction func = (AbstractFunction)function.restrict(signature);
         if(!(func instanceof FailFunction)) {
            restricted.add(func);
         }
      }
      if(restricted.size() > 1) {
         Function result = null;
         for(final AbstractFunction function: restricted) {
            if(!function.isPrivate()) {
               if(result != null) {
                  result = null;
                  break;
               }
               result = function;
            }
         }
         if(result != null) {
            return result;
         }
      }
      if(restricted.size() == 1) {
         return restricted.get(0);
      }
      if(restricted.size() == 0) {
         return new FailFunction(name(), "Arg mismatch");
      }
      return new CompositeFunction(restricted);
   }
}
