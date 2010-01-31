package org.fuwjin.pogo.reflect;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Iterator;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * Creates a new context for the next element of an iterator.
 */
public class NextTask implements InitializerTask {
   private static final String NEXT = "next"; //$NON-NLS-1$
   private static final String FAILED_INVOCATION = "failed invocation"; //$NON-NLS-1$
   private static final String END_OF_ITERATOR = "end of iterator"; //$NON-NLS-1$
   private Invoker invoker;

   @Override
   public boolean equals(final Object obj) {
      try {
         final NextTask o = (NextTask)obj;
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
      if(obj instanceof Iterator<?>) {
         final Iterator<?> iter = (Iterator<?>)obj;
         if(iter.hasNext()) {
            return input.newChild(iter.next(), true, null);
         }
         return input.newChild(null, false, END_OF_ITERATOR);
      }
      final Object result = invoker.invoke(input.get());
      return input.newChild(result, isSuccess(result), FAILED_INVOCATION);
   }

   @Override
   public void setType(final ReflectionType type) {
      invoker = type.getInvoker(NEXT);
   }

   @Override
   public String toString() {
      return NEXT;
   }
}
