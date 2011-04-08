package org.fuwjin.dinah;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.fuwjin.util.TypeUtils;

public class FunctionSignature {
   private final String name;
   private final List<Type> args = new ArrayList<Type>();
   private boolean checkArgs;

   public FunctionSignature(final String name) {
      this.name = name;
   }

   public FunctionSignature(final String name, final int size) {
      this.name = name;
      for(int i = 0; i < size; i++) {
         args.add(null);
      }
      checkArgs = true;
   }

   public void addArg(final String typeName) throws Exception {
      args.add(TypeUtils.forName(typeName));
      checkArgs = true;
   }

   public int argCount() {
      return args.size();
   }

   public Type argType(final int index) {
      return args.get(index);
   }

   public boolean checkArgs() {
      return checkArgs;
   }

   public String name() {
      return name;
   }
}
