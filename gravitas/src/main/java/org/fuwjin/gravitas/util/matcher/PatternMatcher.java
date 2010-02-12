package org.fuwjin.gravitas.util.matcher;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.internal.matchers.TypeSafeMatcher;

public class PatternMatcher extends TypeSafeMatcher<String>{
   public static PatternMatcher matches(final String regex){
      return new PatternMatcher(regex);
   }

   private final Pattern pattern;

   private PatternMatcher(final String pattern){
      this.pattern = Pattern.compile(pattern);
   }

   @Override
   public void describeTo(final Description description){
      description.appendText("matches pattern=").appendValue(pattern);
   }

   @Override
   public boolean matchesSafely(final String obj){
      return pattern.matcher(obj).matches();
   }
   
   @Override
   public String toString(){
      Description desc = new StringDescription();
      describeTo(desc);
      return desc.toString();
   }
}
