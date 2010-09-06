package org.fuwjin.postage.function;

import static java.lang.System.arraycopy;

import java.util.Arrays;

import org.fuwjin.postage.Function;

public class CurriedFunction extends AbstractFunction {
   private final Function function;
   private final Object[] curried;

   public CurriedFunction(final Function function, final Object... args) {
      super(function.signature().curry(args.length));
      this.function = function;
      curried = args;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CurriedFunction o = (CurriedFunction)obj;
         return super.equals(obj) && o.function.equals(function) && Arrays.equals(curried, o.curried);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invokeSafe(final Object... args) {
      final Object[] combined = new Object[curried.length + args.length];
      arraycopy(curried, 0, combined, 0, curried.length);
      arraycopy(args, 0, combined, curried.length, args.length);
      return function.invokeSafe(combined);
   }

   @Override
   protected Object tryInvoke(final Object... args) throws Exception {
      throw new UnsupportedOperationException();
   }
}
