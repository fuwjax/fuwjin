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

   public synchronized void apply(final EventHandler handler){
      final Iterator<Event> iter = events.iterator();
      while(iter.hasNext()){
         final Event event = iter.next();
         try{
            if(handler.handle(getContext(event.source()), event.gesture())){
               iter.remove();
            }
         }catch(Exception e){
            // continue
         }
      }
   }

   public Context getContext(Integration source){
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
      events.add(new Event(source, gesture));
   }
}
