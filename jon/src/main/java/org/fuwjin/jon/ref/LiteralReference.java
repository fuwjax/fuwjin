package org.fuwjin.jon.ref;

public class LiteralReference extends BaseReference {
   private final Object value;

   public LiteralReference(final String name, final Object type, final Object value) {
      super(name, type);
      this.value = value;
   }

   public Object get() {
      return value;
   }
}
