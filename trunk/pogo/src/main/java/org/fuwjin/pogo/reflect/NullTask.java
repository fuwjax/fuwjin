package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;

/**
 * A task that does as little as possible.
 */
public class NullTask implements FinalizerTask, InitializerTask {
   private static final String NULL = "null"; //$NON-NLS-1$

   @Override
   public boolean equals(final Object obj) {
      try {
         final NullTask o = (NullTask)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public void finalize(final PogoContext container, final PogoContext child) {
      // ignore
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public PogoContext initialize(final PogoContext input) {
      return input.newChild(null, true, null);
   }

   @Override
   public void setType(final ReflectionType type) {
      // ignore
   }

   @Override
   public String toString() {
      return NULL;
   }
}
