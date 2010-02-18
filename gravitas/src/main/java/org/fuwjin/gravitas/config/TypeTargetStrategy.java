package org.fuwjin.gravitas.config;


public class TypeTargetStrategy implements TargetStrategy{
   private String type;

   @Override
   public Target newTarget(TargetFactory factory) {
      return factory.newInstance(type);
   }
}
