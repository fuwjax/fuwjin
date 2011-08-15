package org.fuwjin.dinah.signature;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.dinah.adapter.AdapterDecorator;

public class FixedArgsSignature extends AdapterDecorator implements FunctionSignature {
   private final Type category;
   private final String name;
   private final Type[] parameterTypes;

   public FixedArgsSignature(final Adapter adapter, final Type category, final String name, final Type returnType,
         final Type... parameterTypes) {
      super(adapter);
      this.category = category;
      this.name = name;
      this.parameterTypes = parameterTypes;
   }

   @Override
   public Object[] adapt(final Object[] args) throws AdaptException {
      final Object[] values = new Object[args.length];
      for(int i = 0; i < parameterTypes.length; i++) {
         values[i] = adapt(args[i], parameterTypes[i]);
      }
      return values;
   }

   @Override
   public Type argType(final int index) {
      return parameterTypes[index];
   }

   @Override
   public boolean canAdapt(final Type[] types) {
      if(types.length != parameterTypes.length) {
         return false;
      }
      for(int i = 0; i < parameterTypes.length; i++) {
         if(!canAdapt(types[i], parameterTypes[i])) {
            return false;
         }
      }
      return true;
   }

   @Override
   public Type category() {
      return category;
   }

   @Override
   public FunctionSignature meets(final SignatureConstraint constraint) {
      if(constraint.matches(this)) {
         return this;
      }
      return null;
   }

   @Override
   public String memberName() {
      return name;
   }

   @Override
   public String name() {
      try {
         return adapt(category, String.class) + "." + name;
      } catch(final AdaptException e) {
         throw new RuntimeException("String should be a valid conversion for every object", e);
      }
   }

   @Override
   public boolean supportsArgs(final int count) {
      return parameterTypes.length == count;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append('[').append(category().getClass()).append(']');
      builder.append(name());
      String delim = "(";
      for(final Type type: parameterTypes) {
         builder.append(delim).append(type).append('[').append(type.getClass()).append(']');
         delim = ", ";
      }
      return builder.append(')').toString();
   }
}
