package org.fuwjin.pogo.reflect;

import org.fuwjin.pogo.reflect.invoke.Invoker;

/**
 * An abstraction of Class that produces message dispatch objects.
 */
public interface ReflectionType {
   /**
    * Creates a new dispatcher.
    * @param name the name to dispatch
    * @return the dispatcher for the name
    */
   public Invoker getInvoker(String name);

   /**
    * Returns true if {@code input} is an instance of this type.
    * @param input the input to test
    * @return true if input is an instance of this type, false otherwise
    */
   public boolean isInstance(final Object input);
}
