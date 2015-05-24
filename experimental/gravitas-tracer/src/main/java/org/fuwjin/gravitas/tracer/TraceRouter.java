package org.fuwjin.gravitas.tracer;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.inject.Singleton;

@Singleton
public class TraceRouter{
   private final Queue<Trace> traces = new ConcurrentLinkedQueue<Trace>();

   public synchronized int apply(final TraceHandler handler, final int lastId){
      final Iterator<Trace> iter = traces.iterator();
      int ret = lastId;
      while(iter.hasNext()){
         final Trace trace = iter.next();
         if(trace.id() <= lastId){
            continue;
         }
         try{
            if(handler.handle(trace)){
               iter.remove();
            }
         }catch(final Exception e){
            // continue
         }
         ret = trace.id();
      }
      return ret;
   }

   public void trace(Object classifier, int id, String checkpoint){
      traces.add(new Trace(classifier, id, checkpoint));
   }
}
