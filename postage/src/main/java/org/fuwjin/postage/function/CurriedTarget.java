package org.fuwjin.postage.function;

import static java.lang.System.arraycopy;
import static org.fuwjin.postage.type.TypeUtils.isAssignable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.fuwjin.postage.FunctionTarget;

public class CurriedTarget implements FunctionTarget {
   public static FunctionTarget curry(final FunctionTarget target, final Object[] args) {
      for(int index = 0; index < args.length; index++) {
         if(!isAssignable(target.parameterType(index), args[index])) {
            return null;
         }
      }
      return new CurriedTarget(target, args);
   }

   private final Object[] curried;
   private final FunctionTarget target;

   private CurriedTarget(final FunctionTarget target, final Object[] args) {
      this.target = target;
      curried = args;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CurriedTarget o = (CurriedTarget)obj;
         return getClass().equals(o.getClass()) && target.equals(o.target) && Arrays.equals(curried, o.curried);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(curried) + target.hashCode();
   }

   @Override
   public Object invoke(final Object[] args) throws InvocationTargetException, Exception {
      final Object[] combined = new Object[curried.length + args.length];
      arraycopy(curried, 0, combined, 0, curried.length);
      arraycopy(args, 0, combined, curried.length, args.length);
      return target.invoke(combined);
   }

   @Override
   public Type parameterType(final int index) {
      return target.parameterType(index + curried.length);
   }

   @Override
   public int requiredArguments() {
      return Math.max(target.requiredArguments() - curried.length, 0);
   }

   @Override
   public Type returnType() {
      return target.returnType();
   }
}
