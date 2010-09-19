package org.fuwjin.sample;

import java.util.Observable;

public class SampleObject implements TrivialInterface {
   static String staticField;

   public static String sampleStatic(final String arg) {
      return arg;
   }

   String field;
   public Object updated;

   public SampleObject() {
      // ignore
   }

   public SampleObject(final String arg) {
      // ignore
   }

   public void fails() {
      throw new NullPointerException();
   }

   public String sample() {
      return "test";
   }

   @Override
   public void update(final Observable o, final Object arg) {
      updated = arg;
   }
}
