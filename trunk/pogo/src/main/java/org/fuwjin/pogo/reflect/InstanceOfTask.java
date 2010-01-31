package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;

/**
 * Creates a child context if the parent context is of the expected type.
 */
public class InstanceOfTask implements InitializerTask {
   private static final String ERROR_CODE = "IC-"; //$NON-NLS-1$
   private static final String INSTANCEOF = "instanceof "; //$NON-NLS-1$
   private ReflectionType type;

   @Override
   public boolean equals(final Object obj) {
      try {
         final InstanceOfTask o = (InstanceOfTask)obj;
         return eq(getClass(), o.getClass()) && eq(type, o.type);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), type);
   }

   @Override
   public PogoContext initialize(final PogoContext input) {
      final Object obj = input.get();
      return input.newChild(obj, type.isInstance(obj), ERROR_CODE + type);
   }

   @Override
   public void setType(final ReflectionType type) {
      this.type = type;
   }

   @Override
   public String toString() {
      return INSTANCEOF + type;
   }
}
