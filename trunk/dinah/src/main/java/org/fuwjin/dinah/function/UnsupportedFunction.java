package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.util.Adapter.AdaptException;

/**
 * An unsupported function. Designed explicitly to support
 * AbstractFunction.NULL.
 */
public class UnsupportedFunction extends AbstractFunction {
   /**
    * Creates a new instance.
    * @param name the function name
    * @param argTypes the argument types
    */
   public UnsupportedFunction(final String name, final Type... argTypes) {
      super(name, argTypes);
   }

   @Override
   public Object invoke(final Object... args) throws AdaptException, InvocationTargetException {
      throw new UnsupportedOperationException(name());
   }

   @Override
   public AbstractFunction restrict(final FunctionSignature signature) {
      return this;
   }

   @Override
   protected AbstractFunction joinImpl(final AbstractFunction next) {
      return next;
   }
}
