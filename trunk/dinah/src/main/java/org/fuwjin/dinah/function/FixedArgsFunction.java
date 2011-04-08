package org.fuwjin.dinah.function;

import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter;
import org.fuwjin.util.TypeUtils;

public abstract class FixedArgsFunction extends AbstractFunction {
   public FixedArgsFunction(final String name, final Type... argTypes) {
      super(name, argTypes);
   }

   protected abstract void invokeSafe(InvokeResult result, Object[] args);

   @Override
   protected void invokeWithResult(final InvokeResult result, final Object... args) {
      if(args.length != argCount()) {
         result.alert("%s expects %d args not %d", name(), argCount(), args.length);
         return;
      }
      final Object[] realArgs = new Object[args.length];
      for(int i = 0; i < args.length; i++) {
         realArgs[i] = Adapter.adapt(args[i], argType(i), result);
         if(!result.isSuccess()) {
            return;
         }
      }
      invokeSafe(result, realArgs);
   }

   @Override
   public Function restrict(final FunctionSignature signature) {
      if(signature.checkArgs()) {
         if(signature.argCount() != argCount()) {
            return new FailFunction(name(), "%s expected %d args not %d", name(), argCount(), signature.argCount());
         }
         for(int i = 0; i < argCount(); i++) {
            if(!TypeUtils.isAssignableFrom(argType(i), signature.argType(i))) {
               return new FailFunction(name(), "%s expected arg %d to be %s not %s", name(), i, argType(i), signature
                     .argType(i));
            }
         }
      }
      return this;
   }
}
