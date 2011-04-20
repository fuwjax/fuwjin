package org.fuwjin.demo;

import org.fuwjin.ruin.Ruin;
import org.fuwjin.ruin.StandardKeyboard;

public class SampleApp {
   public static void main(final String[] args) throws Exception {
      final Ruin ruin = new Ruin(new StandardKeyboard());
      ruin.open("sample.layout");
      ruin.run("sample.ruin");
   }
}
