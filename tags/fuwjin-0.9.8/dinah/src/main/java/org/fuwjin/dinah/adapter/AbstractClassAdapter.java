package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter.AdaptException;

public abstract class AbstractClassAdapter implements ClassAdapter {
   @Override
   public final <T>T adapt(final Object value, final Class<T> type) throws AdaptException {
      return (T)adaptImpl(value, type);
   }

   @Override
   public boolean canDefault(final Class<?> type) {
      return false;
   }

   @Override
   public final <T>T defaultValue(final Class<T> type) throws AdaptException {
      return (T)defaultImpl(type);
   }

   protected Object adaptImpl(final Object value, final Class<?> type) throws AdaptException {
      throw new AdaptException("could not map %s to %s", value, type);
   }

   protected Object defaultImpl(final Class<?> type) throws AdaptException {
      throw new AdaptException("could not create default for %s", type);
   }
}
