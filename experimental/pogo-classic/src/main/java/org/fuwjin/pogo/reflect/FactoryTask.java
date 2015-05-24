package org.fuwjin.pogo.reflect;

import static org.fuwjin.pogo.reflect.invoke.Invoker.isSuccess;
import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * Creates a new instance from a message.
 */
public class FactoryTask implements InitializerTask {
   private static final String FAILED_FACTORY_METHOD = "failed factory method"; //$NON-NLS-1$
   private String name;
   private Invoker invoker;

   /**
    * Creates a new instance.
    * @param name the name of the message to dispatch
    */
   public FactoryTask(final String name) {
      this.name = name;
   }

   /**
    * Creates a new instance.
    */
   FactoryTask() {
      // for reflection
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final FactoryTask o = (FactoryTask)obj;
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
      final Object result = invoker.invoke(input.get());
      return input.newChild(result, isSuccess(result), FAILED_FACTORY_METHOD);
   }

   @Override
   public void setType(final ReflectionType type) {
      invoker = type.getInvoker(name);
   }

   @Override
   public String toString() {
      return name;
   }
}
