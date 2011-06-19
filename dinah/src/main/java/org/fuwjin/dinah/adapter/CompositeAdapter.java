package org.fuwjin.dinah.adapter;

import static org.fuwjin.dinah.adapter.StandardAdapter.isSet;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fuwjin.dinah.Adapter;

/**
 * A default implementation of Adapter.
 */
public class CompositeAdapter implements Adapter, ClassAdapter {
   private final List<ClassAdapter> adapters;

   public CompositeAdapter() {
      adapters = new ArrayList<ClassAdapter>();
   }

   public CompositeAdapter(final ClassAdapter... adapters) {
      this.adapters = Arrays.asList(adapters);
   }

   @Override
   public <T>T adapt(final Object value, final Class<T> toClass) throws AdaptException {
      if(!isSet(value) && toClass != null) {
         throw new AdaptException("Could not map unset value to %s", toClass);
      }
      if(toClass == null || toClass.isInstance(value)) {
         return (T)value;
      }
      final Class<? extends Object> fromClass = value == null ? null : value.getClass();
      for(final ClassAdapter adapter: adapters) {
         try {
            if(adapter.canAdapt(fromClass, toClass)) {
               return adapter.adapt(value, toClass);
            }
         } catch(final RuntimeException e) {
            e.printStackTrace();
            // continue;
         }
      }
      if(value == null) {
         return null;
      }
      throw new AdaptException("Could not map %s to %s", fromClass, toClass);
   }

   @Override
   public Object adapt(final Object value, final Type type) throws AdaptException {
      return adapt(value, adapt(type, Class.class));
   }

   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      if(toClass.isAssignableFrom(fromClass)) {
         return true;
      }
      for(final ClassAdapter adapter: adapters) {
         if(adapter.canAdapt(fromClass, toClass)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean canAdapt(final Type fromType, final Type toType) {
      try {
         final Class<?> fromClass = adapt(fromType, Class.class);
         final Class<?> toClass = adapt(toType, Class.class);
         return canAdapt(fromClass, toClass);
      } catch(final AdaptException e) {
         return false;
      }
   }

   @Override
   public boolean canDefault(final Class<?> type) {
      for(final ClassAdapter adapter: adapters) {
         if(adapter.canDefault(type)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public <T>T defaultValue(final Class<T> type) throws AdaptException {
      for(final ClassAdapter adapter: adapters) {
         if(adapter.canDefault(type)) {
            return adapter.defaultValue(type);
         }
      }
      throw new AdaptException("Could not create default value for %s", type);
   }
}
