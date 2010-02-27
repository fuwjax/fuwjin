package org.fuwjin.gravitas.test;

import static java.util.Arrays.asList;
import static org.fuwjin.gravitas.Gravitas.startGravitas;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

public class TestIntegration implements Integration{
   public static TestIntegration newIntegration(final Module... modules) throws Exception{
      final Injector injector = startGravitas(asList(modules));
      return injector.getInstance(TestIntegration.class);
   }

   @Inject
   private EventRouter router;
   private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

   public void expectLines(int count) throws InterruptedException{
      for(int i =0;i<count;i++){
         queue.take();
      }
   }

   public void expect(final String expected) throws InterruptedException{
      final String msg = queue.take();
      if(!expected.equals(msg)){
         throw new AssertionError("Expected " + expected + " but found " + msg);
      }
   }

   public void input(final String gesture){
      router.raise(this, gesture);
   }

   public String matches(final String pattern) throws InterruptedException{
      final String msg = queue.take();
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
