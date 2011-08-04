package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter.AdaptException;

public class ObjectToStringAdapter extends AbstractClassAdapter {
   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      return String.class.equals(toClass);
   }

   @Override
   public boolean canDefault(final Class<?> type) {
      return String.class.equals(type);
   }

   @Override
   protected Object adaptImpl(final Object value, final Class<?> type) throws AdaptException {
      return value.toString();
   }

   @Override
   protected Object defaultImpl(final Class<?> type) throws AdaptException {
      return "";
   }
}
