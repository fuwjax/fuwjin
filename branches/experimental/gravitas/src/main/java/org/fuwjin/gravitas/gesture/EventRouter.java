package org.fuwjin.gravitas.gesture;

import static java.util.Collections.unmodifiableCollection;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.inject.Singleton;

@Singleton
public class EventRouter<T>{
   private final BlockingQueue<Event<T>> events = new LinkedBlockingQueue<Event<T>>();

   public synchronized int clear(){
      final int count = events.size();
      events.clear();
      return count;
   }

   public synchronized void apply(final EventHandler<T> handler){
      final Iterator<Event<T>> iter = events.iterator();
      while(iter.hasNext()){
         final Event<T> event = iter.next();
         try{
            if(handler.handle(event.source(), event.gesture())){
               iter.remove();
            }
         }catch(Exception e){
            // continue
         }
      }
   }

   public synchronized Iterable<Event<T>> queue(){
      return unmodifiableCollection(events);
   }

   public void raise(final Integration source, final T gesture){
      events.add(new Event<T>(source, gesture));
   }
}
