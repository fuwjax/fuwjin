package org.fuwjin.jon.builder;

public abstract class LiteralBuilder extends Builder {
   public LiteralBuilder(final Class<?> type) {
      super(type);
   }

   public abstract void set(final String value);
}
