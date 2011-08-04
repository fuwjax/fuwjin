package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.util.PrimitiveInfo;

public class WrapperToPrimitiveAdapter extends AbstractClassAdapter {
   @Override
   public Object adaptImpl(final Object value, final Class<?> type) throws AdaptException {
      final PrimitiveInfo info = PrimitiveInfo.byClass(type);
      if(value instanceof Boolean && info == PrimitiveInfo.BOOL) {
         return value;
      }
      if(value instanceof Character && info == PrimitiveInfo.CHAR) {
         return value;
      }
      if(value instanceof Number) {
         final Number number = (Number)value;
         switch(info) {
         case BYTE:
            return number.byteValue();
         case SHORT:
            return number.shortValue();
         case INT:
            return number.intValue();
         case LONG:
            return number.longValue();
         case FLOAT:
            return number.floatValue();
         case DOUBLE:
            return number.doubleValue();
         default:
            // continue
         }
      }
      return super.adaptImpl(value, type);
   }

   @Override
   public boolean canAdapt(final Class<?> fromClass, final Class<?> toClass) {
      return (Boolean.class.equals(fromClass) || Character.class.equals(fromClass) || Number.class
            .isAssignableFrom(fromClass)) && PrimitiveInfo.byClass(toClass) != null;
   }

   @Override
   public boolean canDefault(final Class<?> type) {
      return PrimitiveInfo.byClass(type) != null;
   }

   @Override
   public Object defaultImpl(final Class<?> type) throws AdaptException {
      switch(PrimitiveInfo.byClass(type)) {
      case BYTE:
         return (byte)0;
      case SHORT:
         return (short)0;
      case INT:
         return 0;
      case LONG:
         return 0L;
      case FLOAT:
         return 0F;
      case DOUBLE:
         return 0D;
      case BOOL:
         return false;
      case CHAR:
         return '\0';
      }
      return super.defaultImpl(type);
   }
}
