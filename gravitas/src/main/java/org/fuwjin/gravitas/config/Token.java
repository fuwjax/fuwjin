package org.fuwjin.gravitas.config;


public interface Token{
   int NOT_APPLIED = -1;

   String toIdent();

   int apply(Target target, String elements, int index);
}
