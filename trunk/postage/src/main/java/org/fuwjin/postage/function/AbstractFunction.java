package org.fuwjin.postage.function;

import java.lang.reflect.AccessibleObject;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.Signature;

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

   public AbstractFunction(final String name, final Class<?>... params) {
      this(new Signature(name, params));
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
   public Signature signature() {
      return signature;
   }
}
