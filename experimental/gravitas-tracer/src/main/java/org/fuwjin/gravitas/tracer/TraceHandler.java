package org.fuwjin.gravitas.tracer;

public interface TraceHandler{
   boolean handle(Trace trace) throws Exception;
}