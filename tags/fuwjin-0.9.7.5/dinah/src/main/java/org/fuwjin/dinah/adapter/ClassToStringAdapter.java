package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter.AdaptException;

public class ClassToStringAdapter extends AbstractClassAdapter {
   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      return Class.class.equals(fromClass) && String.class.equals(toClass);
   }

   @Override
   protected Object adaptImpl(final Object value, final Class<?> type) throws AdaptException {
      return ((Class<?>)value).getCanonicalName();
   }
}
