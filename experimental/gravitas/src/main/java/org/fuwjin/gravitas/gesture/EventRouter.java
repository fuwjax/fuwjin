package org.fuwjin.gravitas.gesture;

import static java.util.Collections.unmodifiableCollection;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.inject.Singleton;

@Singleton
public class EventRouter{
   private final Queue<Event> events = new ConcurrentLinkedQueue<Event>();

   public synchronized int apply(final EventHandler handler, final int lastId){
      final Iterator<Event> iter = events.iterator();
      int ret = lastId;
      while(iter.hasNext()){
         final Event event = iter.next();
         if(event.id() <= lastId){
            continue;
         }
         try{
            if(handler.handle(event) && !event.isBroadcast()){
               iter.remove();
            }
         }catch(final Exception e){
            // continue
         }
         ret = event.id();
      }
      return ret;
   }

   public void broadcast(final Context source, final Object gesture){
      events.add(new Event(source, gesture, true));
   }

   public synchronized int clear(){
      final int count = events.size();
      events.clear();
      return count;
   }

   public synchronized Iterable<Event> queue(){
      return unmodifiableCollection(events);
   }

   public void raise(final Context source, final Object gesture){
      events.add(new Event(source, gesture, false));
   }
}
