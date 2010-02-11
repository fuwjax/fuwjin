package org.fuwjin.gravitas.gesture;

import static java.util.Collections.unmodifiableCollection;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.inject.Singleton;

@Singleton
public class GestureRouter{
   private BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();

   public void raise(Integration source, Object gesture){
      events.add(new Event(source, gesture));
   }

   public void dispatch(GestureHandler handler){
      Iterator<Event> iter = events.iterator();
      while(iter.hasNext()){
         Event event = iter.next();
         if(handler.handle(event.source(), event.gesture())){
            iter.remove();
         }
      }
   }

   public Iterable<Event> queue(){
      return unmodifiableCollection(events);
   }

   public int clear(){
      int count = events.size();
      events.clear();
      return count;
   }
}
