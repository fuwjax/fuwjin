package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import org.fuwjin.dinah.Adapter.AdaptException;

public class ConstantFunction extends FixedArgsFunction {
   private final Object value;

   public ConstantFunction(final String name, final Object value) {
      super(null, null, name);
      this.value = value;
   }

   @Override
   protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
         IllegalArgumentException, IllegalAccessException, InstantiationException {
      return value;
   }
}
