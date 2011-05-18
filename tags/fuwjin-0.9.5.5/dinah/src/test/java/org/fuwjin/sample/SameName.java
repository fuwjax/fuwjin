package org.fuwjin.sample;

/**
 * A class demonstrating 3 ways of having completely identical function
 * signatures.
 */
public class SameName {
   /**
    * Static function with signature
    * org.fuwjin.sample.SameName.name(org.fuwjin.sample.SameName,
    * java.lang.String)
    * @param obj the name object
    * @param value the name value
    */
   public static void name(final SameName obj, final String value) {
      obj.name = "static:" + value;
   }

   /**
    * Instance field with setter signature
    * org.fuwjin.sample.SameName.name(org.fuwjin.sample.SameName,
    * java.lang.String)
    */
   public String name;

   /**
    * Instance function with signature
    * org.fuwjin.sample.SameName.name(org.fuwjin.sample.SameName,
    * java.lang.String, java.lang.String[]), however as it is a var args method,
    * it will also match the signature
    * org.fuwjin.sample.SameName.name(org.fuwjin.sample.SameName,
    * java.lang.String)
    * @param join the join values
    * @param values the name values
    */
   public void name(final String join, final String... values) {
      final StringBuilder builder = new StringBuilder("instance:");
      boolean first = true;
      for(final String value: values) {
         if(first) {
            first = false;
         } else {
            builder.append(join);
         }
         builder.append(value);
      }
      name = builder.toString();
   }
}
