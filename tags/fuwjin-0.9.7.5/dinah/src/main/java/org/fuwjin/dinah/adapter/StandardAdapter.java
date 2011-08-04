package org.fuwjin.dinah.adapter;

import org.fuwjin.dinah.Adapter;

/**
 * A default implementation of Adapter.
 */
public class StandardAdapter extends CompositeAdapter {
   /**
    * Returns true if the value is set.
    * @param value the test value
    * @return true if the value is set, false otherwise
    */
   public static boolean isSet(final Object value) {
      return !Adapter.UNSET.equals(value);
   }

   public StandardAdapter() {
      this(Thread.currentThread().getContextClassLoader());
   }

   public StandardAdapter(final ClassLoader loader) {
      super(new CollectionToArrayAdapter(), new ClassToStringAdapter(), new ObjectToStringAdapter(),
            new StringToClassAdapter(loader), new StringToEnumAdapter(), new StringToPrimitiveAdapter(),
            new WrapperToPrimitiveAdapter());
   }
}
