package org.fuwjin.dinah;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.fuwjin.util.TypeUtils;

public class FunctionSignature {
   private final String name;
   private final List<Type> args = new ArrayList<Type>();
   private int count = -1;

   public FunctionSignature(final String name) {
      this(name, -1);
   }

   public FunctionSignature(final String name, final int count) {
      this.name = name;
      this.count = count;
   }

   public FunctionSignature addArg(final String typeName) throws ClassNotFoundException {
      if(typeName == null || "null".equals(typeName)) {
         args.add(null);
      } else {
         args.add(TypeUtils.forName(typeName));
      }
      return this;
   }

   public int argCount() {
      return count == -1 ? args.size() : count;
   }

   public Type argType(final int index) {
      return index < args.size() ? args.get(index) : null;
   }

   public String name() {
      return name;
   }

   public void setArgCount(final int count) {
      if(args.size() > count) {
         throw new IllegalArgumentException("count " + count + " must be at least " + args.size());
      }
      this.count = count;
   }

   @Override
   public String toString() {
      return name;
   }
}
