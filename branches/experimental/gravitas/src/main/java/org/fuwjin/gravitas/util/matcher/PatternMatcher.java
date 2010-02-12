package org.fuwjin.gravitas.util.matcher;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

public class PatternMatcher extends TypeSafeMatcher<String>{
   public static PatternMatcher matches(String regex){
      return new PatternMatcher(regex);
   }

   private final Pattern pattern;

   public PatternMatcher(String pattern){
      this.pattern = Pattern.compile(pattern);
   }

   @Override
   public boolean matchesSafely(String obj){
      return pattern.matcher(obj).matches();
   }

   @Override
   public void describeTo(Description description){
      description.appendText("matches pattern=").appendValue(pattern);
   }
}
