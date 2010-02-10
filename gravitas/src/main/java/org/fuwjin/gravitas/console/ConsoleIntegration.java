package org.fuwjin.gravitas.console;

import static java.lang.System.in;
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Singleton;

@Singleton
public class ConsoleIntegration implements Integration{
   private BufferedReader reader = new BufferedReader(new InputStreamReader(in));

   @Override
   public void notify(Object... messages){
      for(Object message: messages){
         out.println(message);
      }
   }

   public String readLine() throws IOException{
      return reader.readLine();
   }
}
