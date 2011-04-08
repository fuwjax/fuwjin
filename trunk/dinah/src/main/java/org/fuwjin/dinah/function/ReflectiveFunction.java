package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public abstract class ReflectiveFunction extends FixedArgsFunction {
   public ReflectiveFunction(final String name, final Type... argTypes) {
      super(name, argTypes);
   }

   protected abstract Object invokeImpl(Object[] args) throws InvocationTargetException, Exception;

   @Override
   protected void invokeSafe(final InvokeResult result, final Object[] args) {
      try {
         final Object value = invokeImpl(args);
         result.set(value);
      } catch(final InvocationTargetException e) {
         result.alert(true, e.getCause(), "Unexpected invocation failure in %s", name());
      } catch(final Exception e) {
         result.alert(e, "Arguments did not match %s", name());
      }
   }
}
