package org.fuwjin.gravitas.gesture;

import static java.util.Collections.unmodifiableCollection;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.inject.Singleton;

@Singleton
public class EventRouter{
   private final BlockingQueue<Event> events = new LinkedBlockingQueue<Event>();
   private final ConcurrentMap<String, Context> contexts = new ConcurrentHashMap<String, Context>();

   public synchronized int clear(){
      final int count = events.size();
      events.clear();
      return count;
   }

   public synchronized int apply(final EventHandler handler, int lastId){
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
         }catch(Exception e){
            // continue
         }
         ret = event.id();
      }
      return ret;
   }

   public Context getContext(Integration source){
      if(source instanceof Context){
         return (Context)source;
      }
      Context context = contexts.get(source.name());
      if(context == null){
         context = new Context(source);
         Context old = contexts.putIfAbsent(source.name(), context);
         if(old != null){
            context = old;
         }
      }
      return context;
   }

   public synchronized Iterable<Event> queue(){
      return unmodifiableCollection(events);
   }

   public void raise(final Integration source, final Object gesture){
      events.add(new Event(getContext(source), gesture, false));
   }

   public void broadcast(final Integration source, final Object gesture){
      events.add(new Event(getContext(source), gesture, true));
   }
}
