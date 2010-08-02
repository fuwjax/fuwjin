package org.fuwjin.postage;

public class UnknownSignature extends Signature {
   private final int min;
   private final int max;

   public UnknownSignature(final String name, final int minParams) {
      this(name, minParams, Integer.MAX_VALUE);
   }

   public UnknownSignature(final String name, final int min, final int max) {
      super(name);
      this.min = min;
      this.max = max;
   }

   @Override
   public UnknownSignature curry(final int length) {
      return new UnknownSignature(name(), Math.max(min - length, 0), Math.max(max - length, 0));
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final UnknownSignature o = (UnknownSignature)obj;
         return super.equals(obj) && min == o.min && max == o.max;
      } catch(final RuntimeException e) {
         return false;
      }
   }

   @Override
   public Class<?>[] params(final int count) {
      if(count < min || count > max) {
         return null;
      }
      return new Class<?>[count];
   }
}
