package org.fuwjin.gravitas.test;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fuwjin.gravitas.Gravitas.startGravitas;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import org.fuwjin.gravitas.gesture.Context;
import org.fuwjin.gravitas.gesture.EventRouter;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

public class TestIntegration extends Context{
   public static TestIntegration newIntegration(final Module... modules) throws Exception{
      final Injector injector = startGravitas(asList(modules));
      return injector.getInstance(TestIntegration.class);
   }

   @Inject
   private EventRouter router;
   private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

   public void expectLines(int count) throws Exception{
      for(int i =0;i<count;i++){
         try{
            take();
         }catch(TimeoutException e){
            throw new AssertionError("Expected "+count+" lines but found "+i+" lines");
         }
      }
   }

   private String take() throws Exception{
      String ret = queue.poll(1, SECONDS);
      if(ret == null){
         throw new TimeoutException();
      }
      return ret;
   }

   public void expect(final String expected) throws Exception{
      final String msg = take();
      if(!expected.equals(msg)){
         throw new AssertionError("Expected " + expected + " but found " + msg);
      }
   }

   public void input(final String gesture){
      router.raise(this, gesture);
   }

   public String matches(final String pattern) throws Exception{
      final String msg = take();
      if(!msg.matches(pattern)){
         throw new AssertionError("Expected " + pattern + " but found " + msg);
      }
      return msg;
   }

   @Override
   public String name(){
      return "test";
   }

   @Override
   public void send(final Object... messages){
      for(final Object obj: messages){
         final String[] msg = obj.toString().split("\n");
         queue.addAll(asList(msg));
      }
   }
}
