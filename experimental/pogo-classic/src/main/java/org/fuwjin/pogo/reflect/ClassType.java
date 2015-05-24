package org.fuwjin.pogo.reflect;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * The standard class based reflection type.
 */
public class ClassType implements ReflectionType {
   private Class<?> type;

   /**
    * Creates a new instance.
    * @param type the type to reflect on
    */
   public ClassType(final Class<?> type) {
      this.type = type;
   }

   /**
    * Creates a new instance.
    */
   ClassType() {
      // for reflection
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ClassType o = (ClassType)obj;
         return eq(getClass(), o.getClass()) && eq(type, o.type);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public Invoker getInvoker(final String name) {
      return new Invoker(type, name);
   }

   @Override
   public int hashCode() {
      return hash(getClass(), type);
   }

   @Override
   public boolean isInstance(final Object input) {
      return type.isInstance(input);
   }

   @Override
   public String toString() {
      return type.getName();
   }
}
