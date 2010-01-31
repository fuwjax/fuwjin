package org.fuwjin.jon;

import static org.fuwjin.jon.BuilderRegistry.getBuilder;

import org.fuwjin.jon.builder.Builder;

public class BuilderFactory {
   private Class<?> type;
   private transient Builder builder;

   public Builder forType() {
      builder = getBuilder(type);
      return builder;
   }

   public Object toObject() {
      if(builder == null) {
         throw new RuntimeException();
      }
      return builder.toObject();
   }
}
