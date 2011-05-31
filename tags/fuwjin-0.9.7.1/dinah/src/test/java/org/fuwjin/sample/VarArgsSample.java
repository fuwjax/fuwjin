package org.fuwjin.sample;

import java.util.Arrays;
import org.fuwjin.sample.VarArgsInterface.VarArgsChild;
import org.fuwjin.util.ObjectUtils;
import org.fuwjin.util.ObjectUtils.ObjectHelper;

/**
 * Sample for demonstrating var args.
 */
public class VarArgsSample implements ObjectHelper, VarArgsChild {
   /**
    * Sample static var args method.
    * @param values the var args
    * @return a joined string
    */
   public static String join(final String... values) {
      return Arrays.toString(values);
   }

   private String[] values;

   /**
    * Sample var args constructor.
    * @param values the var args
    */
   public VarArgsSample(final String... values) {
      this.values = values;
   }

   @Override
   public boolean equals(final Object obj) {
      return ObjectUtils.isEqual(this, obj);
   }

   /**
    * Returns the values.
    * @return the values
    */
   public String[] getValues() {
      return values;
   }

   @Override
   public int hashCode() {
      return ObjectUtils.hash(this);
   }

   @Override
   public Object[] identity() {
      return new Object[]{values};
   }

   /**
    * Sets the values.
    * @param values the new values
    */
   @Override
   public void setValues(final String... values) {
      this.values = values;
   }

   @Override
   public String toString() {
      return ObjectUtils.toString(this);
   }
}
