package org.fuwjin.dinah.function;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter;
import org.fuwjin.util.ArrayUtils;
import org.fuwjin.util.TypeUtils;

public class VarArgsFunction extends AbstractFunction {
   private final FixedArgsFunction function;

   public VarArgsFunction(final FixedArgsFunction function) {
      super(function.name(), ArrayUtils.slice(function.argTypes(), 0, -1));
      this.function = function;
   }

   @Override
   protected void invokeWithResult(final InvokeResult result, final Object... args) {
      if(args.length < argCount()) {
         result.alert("%s expects at least %d args not %d", name(), argCount(), args.length);
         return;
      }
      final Object[] realArgs = new Object[function.argCount()];
      for(int i = 0; i < realArgs.length - 1; i++) {
         realArgs[i] = Adapter.adapt(args[i], argType(i), result);
         if(!result.isSuccess()) {
            return;
         }
      }
      final Type type = TypeUtils.getComponentType(function.argType(argCount()));
      if(args.length == function.argCount()
            && TypeUtils.isAssignableFrom(type, args[argCount()].getClass().getComponentType())) {
         realArgs[argCount()] = Adapter.adapt(args[argCount()], function.argType(argCount()), result);
         if(result.isSuccess()) {
            function.invokeSafe(result, realArgs);
            if(result.isSuccess()) {
               return;
            }
         }
         result.reset();
      }
      final Object[] varArgs = new Object[args.length - argCount()];
      realArgs[argCount()] = varArgs;
      for(int i = 0; i < varArgs.length; i++) {
         varArgs[i] = Adapter.adapt(args[i + argCount()], type, result);
         if(!result.isSuccess()) {
            return;
         }
      }
      function.invokeSafe(result, realArgs);
   }

   @Override
   protected Member member() {
      return function.member();
   }

   @Override
   public Function restrict(final FunctionSignature signature) {
      if(signature.checkArgs()) {
         if(signature.argCount() < argCount()) {
            return new FailFunction(name(), "Arg count mismatch");
         }
         for(int index = 0; index < argCount(); index++) {
            if(!TypeUtils.isAssignableFrom(argType(index), signature.argType(index))) {
               return new FailFunction(name(), "Arg mismatch");
            }
         }
         if(signature.argCount() == function.argCount()
               && TypeUtils.isAssignableFrom(function.argType(argCount()), signature.argType(argCount()))) {
            return this;
         }
         final Type componentType = TypeUtils.getComponentType(function.argType(argCount()));
         for(int index = argCount(); index < signature.argCount(); index++) {
            if(!TypeUtils.isAssignableFrom(componentType, signature.argType(index))) {
               return new FailFunction(name(), "Arg mismatch");
            }
         }
      }
      return this;
   }
}
