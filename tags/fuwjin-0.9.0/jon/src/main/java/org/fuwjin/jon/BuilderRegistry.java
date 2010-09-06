/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.jon;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.fuwjin.jon.builder.ArrayBuilder;
import org.fuwjin.jon.builder.Builder;
import org.fuwjin.jon.builder.CharSequenceBuilder;
import org.fuwjin.jon.builder.ClassBuilder;
import org.fuwjin.jon.builder.EnumBuilder;
import org.fuwjin.jon.builder.InstanceBuilder;
import org.fuwjin.jon.builder.ListBuilder;
import org.fuwjin.jon.builder.MapBuilder;
import org.fuwjin.jon.builder.PrimitiveBuilder;
import org.fuwjin.jon.builder.StringInstanceBuilder;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.category.ClassCategory;

public class BuilderRegistry {
   abstract static class ClassFilter {
      private Function invoker;

      public ClassFilter and(final ClassFilter filter) {
         final ClassFilter self = this;
         return new ClassFilter() {
            @Override
            public boolean pass(final Class<?> test) {
               return self.pass(test) && filter.pass(test);
            }
         };
      }

      public ClassFilter create(final Class<? extends Builder> builder) {
         invoker = new ClassCategory(builder).getFunction("new");
         return this;
      }

      public Builder newBuilder(final Class<?> type) {
         return (Builder)invoker.invokeSafe(type);
      }

      public abstract boolean pass(Class<?> test);
   }

   private static List<ClassFilter> builders;
   static {
      builders = new LinkedList<ClassFilter>();
      builders.add(isNull());
      builders.add(isEnum().create(EnumBuilder.class));
      builders.add(isArray().create(ArrayBuilder.class));
      builders.add(isPrimitive().create(PrimitiveBuilder.class));
      builders.add(isWrapper().create(PrimitiveBuilder.class));
      builders.add(isAssignableTo(String.class).create(StringInstanceBuilder.class));
      builders.add(isAssignableTo(Class.class).create(ClassBuilder.class));
      builders.add(isAssignableTo(List.class).and(isConstructor()).create(ListBuilder.class));
      builders.add(isAssignableTo(Map.class).and(isConstructor()).create(MapBuilder.class));
      builders.add(isAssignableTo(CharSequence.class).and(isConstructor(String.class))
            .create(CharSequenceBuilder.class));
      builders.add(otherwise().create(InstanceBuilder.class));
   }

   public static Builder getBuilder(final Class<?> type) {
      try {
         for(final ClassFilter filter: builders) {
            if(filter.pass(type)) {
               return filter.newBuilder(type);
            }
         }
         return null;
      } catch(final Exception e) {
         throw new RuntimeException(e);
      }
   }

   private static ClassFilter isArray() {
      return new ClassFilter() {
         @Override
         public boolean pass(final Class<?> test) {
            return test.isArray();
         }
      };
   }

   private static ClassFilter isAssignableTo(final Class<?> type) {
      return new ClassFilter() {
         @Override
         public boolean pass(final Class<?> test) {
            return type.isAssignableFrom(test);
         }
      };
   }

   private static ClassFilter isConstructor(final Class<?>... params) {
      return new ClassFilter() {
         @Override
         public boolean pass(final Class<?> test) {
            try {
               test.getDeclaredConstructor(params);
               return true;
            } catch(final Exception e) {
               return false;
            }
         }
      };
   }

   private static ClassFilter isEnum() {
      return new ClassFilter() {
         @Override
         public boolean pass(final Class<?> test) {
            return test.isEnum();
         }
      };
   }

   private static ClassFilter isNull() {
      return new ClassFilter() {
         @Override
         public Builder newBuilder(final Class<?> type) {
            return null;
         }

         @Override
         public boolean pass(final Class<?> test) {
            return test == null;
         }
      };
   }

   private static ClassFilter isPrimitive() {
      return new ClassFilter() {
         @Override
         public boolean pass(final Class<?> test) {
            return test.isPrimitive();
         }
      };
   }

   private static ClassFilter isWrapper() {
      return new ClassFilter() {
         @Override
         public boolean pass(final Class<?> test) {
            return PrimitiveBuilder.isWrapper(test);
         }
      };
   }

   private static ClassFilter otherwise() {
      return new ClassFilter() {
         @Override
         public boolean pass(final Class<?> test) {
            return true;
         }
      };
   }
}
