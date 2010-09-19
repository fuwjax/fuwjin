package org.fuwjin.postage;

public enum StandardAdaptable implements Adaptable {
   TRUE {
      @Override
      public Object as(final Class<?> type) {
         if(boolean.class.equals(type) || type.isAssignableFrom(Boolean.class)) {
            return true;
         }
         return fail(type);
      }
   },
   FALSE {
      @Override
      public Object as(final Class<?> type) {
         if(boolean.class.equals(type) || type.isAssignableFrom(Boolean.class)) {
            return false;
         }
         return fail(type);
      }
   },
   NULL {
      @Override
      public Object as(final Class<?> type) {
         if(type.equals(String.class)) {
            return "null";
         }
         return null;
      }
   },
   UNSET {
      @Override
      public Object as(final Class<?> type) {
         return fail(type);
      }
   },
   DEFAULT {
      @Override
      public Object as(final Class<?> type) {
         return AdaptableUtils.getHandler(type).getDefault();
      }
   };
   protected Failure fail(final Class<?> type) {
      return AdaptableUtils.fail(name(), type);
   }
}
