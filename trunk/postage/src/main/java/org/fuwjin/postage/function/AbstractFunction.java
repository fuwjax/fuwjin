package org.fuwjin.postage.function;

import java.lang.reflect.AccessibleObject;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Signature;
import org.fuwjin.postage.StandardAdaptable;

public abstract class AbstractFunction implements Function {
   protected static <T extends AccessibleObject> T access(final T obj) {
      if(!obj.isAccessible()) {
         obj.setAccessible(true);
      }
      return obj;
   }

   private final Signature signature;

   public AbstractFunction(final Signature signature) {
      this.signature = signature;
   }

   public AbstractFunction(final String name, final Class<?> returnType, final boolean isVarArgs,
         final Class<?>... params) {
      this(new Signature(name, returnType, isVarArgs, params));
   }

   @Override
   public final Function curry(final Object... args) {
      return new CurriedFunction(this, args);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final AbstractFunction o = (AbstractFunction)obj;
         return getClass().equals(obj.getClass()) && signature.equals(o.signature);
      } catch(final Exception e) {
         return false;
      }
   }

   protected Failure failure(final String pattern, final Object... args) {
      return new Failure(pattern, args);
   }

   protected Failure failure(final Throwable cause, final String pattern, final Object... args) {
      return new Failure(cause, pattern, args);
   }

   @Override
   public final Object invoke(final Object... args) throws Failure {
      final Object result = invokeSafe(args);
      if(result instanceof Failure) {
         throw (Failure)result;
      }
      return result;
   }

   @Override
   public Object invokeSafe(final Object... args) {
      final Object[] realArgs = signature.checkArgs(args);
      if(realArgs.length == 1 && realArgs[0] instanceof Failure) {
         return realArgs[0];
      }
      try {
         final Object result = tryInvoke(realArgs);
         return signature.checkResult(result);
      } catch(final Exception e) {
         return failure(e, "Unexpected Failure", this, args);
      }
   }

   @Override
   public String name() {
      return signature.name();
   }

   @Override
   public final Function optional(final Object arg) {
      if(arg == StandardAdaptable.UNSET) {
         return this;
      }
      return new OptionalFunction(this, arg);
   }

   @Override
   public Signature signature() {
      return signature;
   }

   @Override
   public String toString() {
      return name();
   }

   protected abstract Object tryInvoke(Object... args) throws Exception;
}
