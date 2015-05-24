package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import org.junit.Test;


public class DriftTest{
   @Test
   public void testDrift() throws IOException{
      PrintStream out = System.out;
      PipedInputStream sink = new PipedInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(sink));
      System.setOut(new PrintStream(new PipedOutputStream(sink)));
      
      doSomething();
      
      System.setOut(out);
      String line = reader.readLine();
      assertThat(line, is("org/fuwjin/test/DriftTest#doSomething"));
   }

   private void doSomething(){
   }
}
