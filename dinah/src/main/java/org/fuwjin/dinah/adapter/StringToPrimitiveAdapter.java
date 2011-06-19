package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.util.PrimitiveInfo;

public class StringToPrimitiveAdapter extends AbstractClassAdapter {
   @Override
   public Object adaptImpl(final Object value, final Class<?> type) throws AdaptException {
      final String s = (String)value;
      switch(PrimitiveInfo.byClass(type)) {
      case BYTE:
         return Byte.parseByte(s);
      case SHORT:
         return Short.parseShort(s);
      case INT:
         try {
            return Integer.parseInt(s);
         } catch(final NumberFormatException e) {
            throw new AdaptException(e, "could not map %s to integer", s);
         }
      case LONG:
         return Long.parseLong(s);
      case FLOAT:
         return Float.parseFloat(s);
      case DOUBLE:
         return Double.parseDouble(s);
      case BOOL:
         return Boolean.parseBoolean(s);
      case CHAR:
         return s.charAt(0);
      }
      return super.adaptImpl(value, type);
   }

   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      return String.class.equals(fromClass) && PrimitiveInfo.byClass(toClass) != null;
   }
}
