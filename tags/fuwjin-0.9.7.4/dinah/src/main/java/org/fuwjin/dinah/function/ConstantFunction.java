package org.fuwjin.dinah.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.signature.FixedArgsSignature;

public class ConstantFunction extends AbstractFunction {
   private final Object value;

   public ConstantFunction(final Adapter adapter, final Type category, final String name, final Object value) {
      super(new FixedArgsSignature(adapter, category, name, value == null ? Object.class : value.getClass()));
      this.value = value;
   }

   @Override
   protected Object invokeSafe(final Object... args) throws AdaptException, InvocationTargetException,
         IllegalArgumentException, IllegalAccessException, InstantiationException {
      return value;
   }
}
