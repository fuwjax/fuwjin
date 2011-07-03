package org.fuwjin.dinah.adapter;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;

public class AdapterDecorator implements Adapter {
   private final Adapter adapter;

   public AdapterDecorator(final Adapter adapter) {
      this.adapter = adapter;
   }

   @Override
   public <T>T adapt(final Object value, final Class<T> type) throws AdaptException {
      try {
         return type.cast(adapter.adapt(value, type));
      } catch(final NullPointerException e) {
         e.printStackTrace();
         throw e;
      }
   }

   @Override
   public Object adapt(final Object value, final Type type) throws AdaptException {
      return adapter.adapt(value, type);
   }

   @Override
   public boolean canAdapt(final Type fromType, final Type toType) {
      return adapter.canAdapt(fromType, toType);
   }
}
