package org.fuwjin.test;

import static java.util.Arrays.asList;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.fuwjin.gravitas.gesture.EventRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class TestIntegration implements Integration{
   @Inject
   private EventRouter<String> router;
   private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

   @Override
   public void send(Object... messages){
      for(Object obj: messages){
         String[] msg = obj.toString().split("\n");
         queue.addAll(asList(msg));
      }
   }

   public void input(String gesture){
      router.raise(this, gesture);
   }

   public void expect(String expected) throws InterruptedException{
      String msg = queue.take();
      if(!expected.equals(msg)){
         throw new AssertionError("Expected "+expected+" but found "+msg);
      }
   }

   public boolean isClear(){
      return queue.isEmpty();
   }

   public void clearOutput(){
      queue.clear();
   }

   public String matches(String pattern) throws InterruptedException{
      String msg = queue.take();
      if(!msg.matches(pattern)){
         throw new AssertionError("Expected "+pattern+" but found "+msg);
      }
      return msg;
   }
}
