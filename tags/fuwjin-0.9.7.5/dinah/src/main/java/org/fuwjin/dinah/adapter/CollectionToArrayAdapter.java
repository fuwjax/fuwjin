package org.fuwjin.dinah.adapter;

import java.lang.reflect.Array;
import java.util.Collection;
import org.fuwjin.dinah.Adapter.AdaptException;

public class CollectionToArrayAdapter extends AbstractClassAdapter {
   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      return Collection.class.isAssignableFrom(fromClass) && toClass.isArray();
   }

   @Override
   public boolean canDefault(final Class<?> type) {
      return type.isArray();
   }

   @Override
   protected Object adaptImpl(final Object value, final Class<?> type) throws AdaptException {
      final Collection<?> collection = (Collection<?>)value;
      final Class<?> component = type.getComponentType();
      final Object arr = Array.newInstance(component, collection.size());
      int i = 0;
      for(final Object obj: collection) {
         Array.set(arr, i++, obj);
      }
      return arr;
   }

   @Override
   protected Object defaultImpl(final Class<?> type) throws AdaptException {
      final Class<?> component = type.getComponentType();
      return Array.newInstance(component, 0);
   }
}
