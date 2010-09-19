package org.fuwjin.postage;

public enum StandardHandler implements TypeHandler {
   BOOLEAN(false, boolean.class, Boolean.class) {
      @Override
      public Object toObject(final String str) {
         if("false".equalsIgnoreCase(str)) {
            return false;
         }
         if("true".equalsIgnoreCase(str)) {
            return true;
         }
         return fail(str);
      }
   },
   BYTE(0, byte.class, Byte.class) {
      @Override
      public Object toObject(final String str) {
         return Byte.valueOf(str);
      }
   },
   CHAR(0, char.class, Character.class) {
      @Override
      public Object toObject(final String str) {
         if(str.length() == 1) {
            fail(str);
         }
         return str.charAt(0);
      }
   },
   DOUBLE(0.0, double.class, Double.class) {
      @Override
      public Object toObject(final String str) {
         return Double.valueOf(str);
      }
   },
   FLOAT(0.0f, float.class, Float.class) {
      @Override
      public Object toObject(final String str) {
         return Float.valueOf(str);
      }
   },
   INTEGER(0, int.class, Integer.class) {
      @Override
      public Object toObject(final String str) {
         return Integer.valueOf(str);
      }
   },
   LONG(0L, long.class, Long.class) {
      @Override
      public Object toObject(final String str) {
         return Long.valueOf(str);
      }
   },
   SHORT(0, short.class, Short.class) {
      @Override
      public Object toObject(final String str) {
         return Short.valueOf(str);
      }
   },
   STRING("", String.class, String.class) {
      @Override
      public Object toObject(final String str) {
         return str;
      }
   },
   ARRAY(new Object[0], Object[].class, Object[].class) {
      @Override
      public Object toObject(final String str) {
         return fail(str);
      }
   },
   OBJECT(null, Object.class, Object.class) {
      @Override
      public Object toObject(final String str) {
         return fail(str);
      }
   };
   private static final StandardHandler[] vals = values();

   public static TypeHandler getHandler(final Class<?> type) {
      for(final StandardHandler handler: vals) {
         if(handler.primitive.isAssignableFrom(type) || handler.wrapper.isAssignableFrom(type)) {
            return handler;
         }
      }
      return type.isArray() ? ARRAY : OBJECT;
   }

   private final Object defaultValue;
   private final Class<?> wrapper;
   private final Class<?> primitive;

   private StandardHandler(final Object defaultValue, final Class<?> primitive, final Class<?> wrapper) {
      this.defaultValue = defaultValue;
      this.primitive = primitive;
      this.wrapper = wrapper;
   }

   protected Failure fail(final Object value) {
      return AdaptableUtils.fail(value, wrapper);
   }

   @Override
   public Object getDefault() {
      return defaultValue;
   }

   @Override
   public boolean isWrapper(final Class<?> test) {
      return wrapper.equals(test);
   }
}
