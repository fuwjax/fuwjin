package org.fuwjin.postage;

import static org.fuwjin.postage.AdaptableUtils.adapt;

public class Signature {
   private final String name;
   private final Class<?>[] params;
   private final Class<?> returnType;
   private final boolean isVarArgs;

   public Signature(final String name, final Class<?> returnType, final boolean isVarArgs, final Class<?>... params) {
      this.name = name;
      this.returnType = returnType;
      this.isVarArgs = isVarArgs;
      this.params = params;
   }

   public Object[] checkArgs(final Object... args) {
      final Object[] realArgs = new Object[params.length];
      int len = params.length;
      if(isVarArgs) {
         if(args.length + 1 < params.length) {
            return new Object[]{new Failure("expected at least %d args, received %d", params.length - 1, args.length)};
         }
         len--;
         // if(args.length == params.length) {
         // realArgs[len] = adapt(params[len], args[len]);
         // }
         if(realArgs[len] == null || realArgs[len] instanceof Failure) {
            final Class<?> varType = params[len].getComponentType();
            final Object[] varArgs = new Object[args.length - len];
            for(int i = len; i < args.length; i++) {
               varArgs[i - len] = adapt(varType, args[i]);
               if(varArgs[i - len] instanceof Failure) {
                  return new Object[]{varArgs[i - len]};
               }
            }
            realArgs[len] = varArgs;
         }
      } else {
         if(args.length != params.length) {
            return new Object[]{new Failure("expected %d args, received %d", params.length, args.length)};
         }
      }
      for(int i = 0; i < len; i++) {
         realArgs[i] = adapt(params[i], args[i]);
         if(realArgs[i] instanceof Failure) {
            return new Object[]{realArgs[i]};
         }
      }
      return realArgs;
   }

   public Object checkResult(final Object result) {
      return adapt(returnType, result);
   }

   public Signature curry(final int length) {
      if(isVarArgs && length >= params.length) {
         return new Signature(name, returnType, isVarArgs, params[params.length - 1]);
      }
      final Class<?>[] newParams = new Class<?>[params.length - length];
      System.arraycopy(params, length, newParams, 0, newParams.length);
      return new Signature(name, returnType, isVarArgs, newParams);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Signature o = (Signature)obj;
         return name.equals(o.name);
      } catch(final RuntimeException e) {
         return false;
      }
   }

   public String name() {
      return name;
   }

   public Class<?>[] params(final int count) {
      if(params.length == count) {
         return params;
      }
      return null;
   }

   public Class<?> returnType() {
      return returnType;
   }

   @Override
   public String toString() {
      return name;
   }
}
