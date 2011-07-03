package org.fuwjin.dinah.adapter;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.util.PrimitiveInfo;

public class StringToClassAdapter extends AbstractClassAdapter {
   private static final ThreadLocal<ClassLoader> loader = new ThreadLocal<ClassLoader>() {
      @Override
      protected ClassLoader initialValue() {
         return Thread.currentThread().getContextClassLoader();
      }
   };

   private static String toArray(final String name) {
      final StringBuilder builder = new StringBuilder();
      int index = name.length();
      while(index >= 2 && name.charAt(index - 2) == '[' && name.charAt(index - 1) == ']') {
         index = index - 2;
         builder.append('[');
      }
      final String subName = name.substring(0, index);
      final PrimitiveInfo info = PrimitiveInfo.byPrimitiveName(subName);
      if(info != null) {
         builder.append(info.shortName());
      } else {
         builder.append('L').append(subName).append(';');
      }
      return builder.toString();
   }

   private final ClassLoader override;

   public StringToClassAdapter() {
      this(null);
   }

   public StringToClassAdapter(final ClassLoader loader) {
      override = loader;
   }

   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      return String.class.equals(fromClass) && (Class.class.equals(toClass) || Type.class.equals(toClass));
   }

   @Override
   public boolean canDefault(final Class<?> type) {
      return Class.class.equals(type);
   }

   @Override
   protected Object adaptImpl(final Object value, final Class<?> type) throws AdaptException {
      String name = (String)value;
      final PrimitiveInfo info = PrimitiveInfo.byPrimitiveName(name);
      if(info != null) {
         return info.primitive();
      }
      try {
         if(name.endsWith("[]")) {
            return Class.forName(toArray(name), true, loader());
         }
         return Class.forName(name, true, loader());
      } catch(final ClassNotFoundException e) {
         int index = name.lastIndexOf('.');
         while(index >= 0) {
            name = name.substring(0, index) + '$' + name.substring(index + 1);
            try {
               return Class.forName(name, true, loader());
            } catch(final ClassNotFoundException ex) {
               index = name.lastIndexOf('.');
            }
         }
         throw new AdaptException(e, "Could not find class named %s", value);
      }
   }

   @Override
   protected Object defaultImpl(final Class<?> type) throws AdaptException {
      return Object.class;
   }

   private ClassLoader loader() {
      if(override != null) {
         return override;
      }
      return loader.get();
   }
}
