package org.fuwjin.jon.ref;

public class StringReference extends BaseReference {
   private final String value;

   public StringReference(final String name, final Object type, final Object value) {
      super(name, type);
      this.value = value.toString();
   }

   public Object get() {
      return value;
   }
}
