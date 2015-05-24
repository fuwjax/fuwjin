package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;

/**
 * Creates a new context from the parent context.
 */
public class PassThruTask implements InitializerTask {
   private static final String THIS = "this"; //$NON-NLS-1$

   @Override
   public boolean equals(final Object obj) {
      try {
         final PassThruTask o = (PassThruTask)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public PogoContext initialize(final PogoContext input) {
      return input.newChild(input.get(), true, null);
   }

   @Override
   public void setType(final ReflectionType type) {
      // ignore
   }

   @Override
   public String toString() {
      return THIS;
   }
}
