package org.fuwjin.postage;

public class Signature {
   private final String name;
   private final Class<?>[] params;

   public Signature(final String name, final Class<?>... params) {
      this.name = name;
      this.params = params;
   }

   public Signature curry(final int length) {
      if(length >= params.length) {
         return new Signature(name);
      }
      final Class<?>[] newParams = new Class<?>[params.length - length];
      System.arraycopy(params, length, newParams, 0, newParams.length);
      return new Signature(name, newParams);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Signature o = (Signature)obj;
         return name.equals(o.name);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   public String name() {
      return name;
   }

   public Class<?>[] params(final int count) {
      if(params.length == count) {
         return params;
      }
      return null;
   }

   @Override
   public String toString() {
      return name;
   }
}
