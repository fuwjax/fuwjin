package org.fuwjin.gravitas.gesture;

import static java.util.Collections.unmodifiableCollection;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.inject.Singleton;

@Singleton
public class GestureRouter{
   private final BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();

   public synchronized int clear(){
      final int count = events.size();
      events.clear();
      return count;
   }

   public synchronized void dispatch(final GestureHandler handler){
      final Iterator<Event> iter = events.iterator();
      while(iter.hasNext()){
         final Event event = iter.next();
         if(handler.handle(event.source(), event.gesture())){
            iter.remove();
         }
      }
   }

   public synchronized Iterable<Event> queue(){
      return unmodifiableCollection(events);
   }

   public void raise(final Integration source, final Object gesture){
      events.add(new Event(source, gesture));
   }
}
