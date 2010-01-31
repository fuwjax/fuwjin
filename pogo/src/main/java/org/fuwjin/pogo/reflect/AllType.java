package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * A reflection type that produces empty dispatches.
 */
public class AllType implements ReflectionType {
   private static final String ALL = "all"; //$NON-NLS-1$

   @Override
   public boolean equals(final Object obj) {
      try {
         final AllType o = (AllType)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public Invoker getInvoker(final String name) {
      return Invoker.NULL;
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public boolean isInstance(final Object input) {
      return true;
   }

   @Override
   public String toString() {
      return ALL;
   }
}
