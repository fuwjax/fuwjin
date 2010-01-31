package org.fuwjin.pogo.reflect;

import org.fuwjin.io.PogoContext;

/**
 * A task run before a rule is started.
 */
public interface InitializerTask {
   /**
    * Creates a child context.
    * @param input the parent context
    * @return the child context
    */
   PogoContext initialize(PogoContext input);

   /**
    * Sets the message dispatch type.
    * @param type the type to source for dispatch
    */
   void setType(ReflectionType type);
}
