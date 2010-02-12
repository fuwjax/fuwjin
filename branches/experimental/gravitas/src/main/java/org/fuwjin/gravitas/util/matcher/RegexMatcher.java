package org.fuwjin.gravitas.util.matcher;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

public class RegExMatcher extends TypeSafeMatcher<String>{
   private final String regex;

   public RegExMatcher(String regex){
      this.regex = regex;
   }

   @Override
   public boolean matchesSafely(String o){
      return o.matches(regex);
   }

   @Override
   public void describeTo(Description description){
      description.appendText("matches regex=").appendValue(regex);
   }

   public static RegExMatcher matches(String regex){
      return new RegExMatcher(regex);
   }
}
