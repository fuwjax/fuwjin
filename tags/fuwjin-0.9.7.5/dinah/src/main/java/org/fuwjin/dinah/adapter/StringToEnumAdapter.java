package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter.AdaptException;

public class StringToEnumAdapter extends AbstractClassAdapter {
   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      return String.class.equals(fromClass) && toClass.isEnum();
   }

   @Override
   protected Object adaptImpl(final Object value, final Class type) throws AdaptException {
      final String name = (String)value;
      return Enum.valueOf(type, name);
   }
}
