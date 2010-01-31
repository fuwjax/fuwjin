package org.fuwjin.pogo.reflect;

import static org.fuwjin.pogo.reflect.DefaultResultTask.MATCH;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;

/**
 * Creates a new context that expects to be filled with the matched parse.
 */
public class ReferenceTask implements InitializerTask {
   private ReflectionType type;

   @Override
   public boolean equals(final Object obj) {
      try {
         final ReferenceTask o = (ReferenceTask)obj;
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
      final Object obj = input.get();
      // return input.newChild(type.isInstance(obj) ? obj : MATCH, obj == null
      // || type.isInstance(obj), null);
      return input.newChild(type.isInstance(obj) ? obj : MATCH, true, null);
   }

   @Override
   public void setType(final ReflectionType type) {
      this.type = type;
   }
}
