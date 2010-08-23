package org.fuwjin.postage.function;

import static java.lang.System.arraycopy;

import org.fuwjin.postage.CompositeFailure;
import org.fuwjin.postage.CompositeSignature;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;

public class OptionalFunction extends AbstractFunction implements Function {
   private final Function function;
   private final Object arg;

   public OptionalFunction(final Function function, final Object arg) {
      super(new CompositeSignature(function.name()));
      this.function = function;
      this.arg = arg;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final OptionalFunction o = (OptionalFunction)obj;
         return super.equals(obj) && o.function.equals(function) && (arg == null ? o.arg == null : arg.equals(o.arg));
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Object invokeSafe(final Object... args) {
      final Object[] combined = new Object[1 + args.length];
      arraycopy(args, 0, combined, 1, args.length);
      combined[0] = arg;
      final Object result = function.invokeSafe(combined);
      final Object result2 = result instanceof Failure ? function.invokeSafe(args) : result;
      if(result2 instanceof Failure) {
         final CompositeFailure failure = new CompositeFailure("Optional arg failed: ", arg);
         failure.addFailure((Failure)result);
         failure.addFailure((Failure)result2);
         return failure;
      }
      return result;
   }

   @Override
   protected Object tryInvoke(final Object... args) throws Exception {
      throw new UnsupportedOperationException();
   }
}
