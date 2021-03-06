package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;

/**
 * Sets the parent container to the matched parse result.
 */
public class DefaultResultTask implements FinalizerTask {
   private static final String DEFAULT = "default"; //$NON-NLS-1$
   private static final String FAILED_RETURN = "failed return"; //$NON-NLS-1$
   /**
    * Indicates that the container should be replaced with the match from the
    * child.
    */
   public static final Object MATCH = new Object();

   @Override
   public boolean equals(final Object obj) {
      try {
         final DefaultResultTask o = (DefaultResultTask)obj;
         return eq(getClass(), o.getClass());
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public void finalize(final PogoContext container, final PogoContext child) {
      final Object obj = child.get();
      if(obj == MATCH) {
         container.set(child.match(), true, null);
      } else {
         container.set(obj, true, FAILED_RETURN);
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public void setType(final ReflectionType type) {
      // ignore
   }

   @Override
   public String toString() {
      return DEFAULT;
   }
}
