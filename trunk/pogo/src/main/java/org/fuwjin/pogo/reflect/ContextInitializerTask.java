package org.fuwjin.pogo.reflect;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * Creates a child context if the parent context is of the expected type.
 */
public class ContextInitializerTask implements InitializerTask {
   private static final String CONTEXT = "context."; //$NON-NLS-1$
   private static final String FAILED_INVOKE = "failed context invoke"; //$NON-NLS-1$
   private String name;
   private Invoker invoker;

   /**
    * Creates a new instance.
    * @param name the context message to dispatch
    */
   public ContextInitializerTask(final String name) {
      this.name = name;
   }

   /**
    * Creates a new instance.
    */
   ContextInitializerTask() {
      // for reflection
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ContextInitializerTask o = (ContextInitializerTask)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name);
   }

   @Override
   public PogoContext initialize(final PogoContext input) {
      if(invoker == null) {
         invoker = new Invoker(input.getClass(), name);
      }
      final Object obj = invoker.invoke(input);
      return input.newChild(obj, isSuccess(obj), FAILED_INVOKE);
   }

   @Override
   public void setType(final ReflectionType type) {
      // ignore
   }

   @Override
   public String toString() {
      return CONTEXT + name;
   }
}
