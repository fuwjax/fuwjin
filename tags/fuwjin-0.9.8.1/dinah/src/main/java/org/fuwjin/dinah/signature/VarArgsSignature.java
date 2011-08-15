package org.fuwjin.dinah.signature;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.util.TypeUtils;

public class VarArgsSignature extends FixedArgsSignature {
   public static FunctionSignature newSignature(final Adapter adapter, final Type category, final String name,
         final Type returnType, final Type[] parameterTypes, final boolean varArgs) {
      if(varArgs) {
         return new VarArgsSignature(adapter, category, name, returnType, parameterTypes);
      }
      return new FixedArgsSignature(adapter, category, name, returnType, parameterTypes);
   }

   private final int required;
   private final Type componentType;

   public VarArgsSignature(final Adapter adapter, final Type category, final String name, final Type returnType,
         final Type... parameterTypes) {
      super(adapter, category, name, returnType, parameterTypes);
      required = parameterTypes.length - 1;
      componentType = TypeUtils.getComponentType(parameterTypes[required]);
   }

   @Override
   public Object[] adapt(final Object[] args) throws AdaptException {
      if(args.length == required + 1 && TypeUtils.isAssignableFrom(argType(required), args[required].getClass())) {
         return super.adapt(args);
      }
      final Object[] values = new Object[required + 1];
      for(int i = 0; i < required; i++) {
         values[i] = adapt(args[i], argType(i));
      }
      values[required] = TypeUtils.newArrayInstance(componentType, args.length - required);
      for(int i = required; i < args.length; i++) {
         Array.set(values[required], i - required, adapt(args[i], componentType));
      }
      return values;
   }

   @Override
   public boolean canAdapt(final Type[] types) {
      if(super.canAdapt(types)) {
         return true;
      }
      if(!supportsArgs(types.length)) {
         return false;
      }
      for(int i = 0; i < required; i++) {
         if(!canAdapt(types[i], argType(i))) {
            return false;
         }
      }
      for(int i = required; i < types.length; i++) {
         if(!canAdapt(types[i], componentType)) {
            return false;
         }
      }
      return true;
   }

   @Override
   public FunctionSignature meets(final SignatureConstraint constraint) {
      final FunctionSignature signature = super.meets(constraint);
      if(signature != null) {
         return signature;
      }
      return super.meets(constraint);
   }

   @Override
   public boolean supportsArgs(final int count) {
      return count >= required;
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append(name());
      String delim = "(";
      for(int i = 0; i < required; i++) {
         builder.append(delim).append(argType(i));
         delim = ", ";
      }
      return builder.append(delim).append(argType(required)).append("... )").toString();
   }
}
