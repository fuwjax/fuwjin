package org.fuwjin.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Matchers {
   public static <T>Matcher<T> stringValue(final T value) {
      return new BaseMatcher<T>() {
         @Override
         public void describeTo(final Description description) {
            description.appendValue("(" + value.getClass().getCanonicalName() + ")" + value);
         }

         @Override
         public boolean matches(final Object item) {
            if(item == null) {
               return value == null;
            }
            if(value == null) {
               return false;
            }
            return item.getClass().equals(value.getClass()) && item.toString().equals(value.toString());
         }
      };
   }
}
