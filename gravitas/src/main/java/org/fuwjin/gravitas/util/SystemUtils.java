package org.fuwjin.gravitas.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.fuwjin.gravitas.util.pipe.OutputStreamToReader;
import org.fuwjin.gravitas.util.pipe.WriterToInputStream;

public class SystemUtils{
   private static final InputStream origIn = System.in;
   private static final PrintStream origOut = System.out;
   private static final PrintStream origErr = System.err;
   private static final OutputStreamToReader outPipe = new OutputStreamToReader();
   private static final OutputStreamToReader errPipe = new OutputStreamToReader();
   private static final WriterToInputStream inPipe = new WriterToInputStream();
   public static final PrintWriter in = new PrintWriter(inPipe.writer());
   public static final BufferedReader out = new BufferedReader(outPipe.reader());
   public static final BufferedReader err = new BufferedReader(errPipe.reader());

   public static synchronized void buffer(){
      if(System.in == origIn){
         System.setOut(new PrintStream(outPipe.outputStream()));
         System.setErr(new PrintStream(errPipe.outputStream()));
         System.setIn(inPipe.inputStream());
      }
   }

   public static synchronized void release(){
      if(System.in == inPipe.inputStream()){
         System.setIn(origIn);
         System.setErr(origErr);
         System.setOut(origOut);
      }
   }

   public static void clearOut() throws IOException{
      while(out.ready()){
         out.skip(1);
      }
   }

   public static void debug(){
      inPipe.logTo(origOut);
      outPipe.logTo(origOut);
      errPipe.logTo(origOut);
   }
}
