package org.fuwjin.dinah.function;

import java.lang.reflect.Member;
import java.lang.reflect.Type;

import org.fuwjin.util.TypeUtils;

public class InstanceOfFunction extends FixedArgsFunction {
   private final Type type;

   public InstanceOfFunction(final String typeName, final Type type) {
      super(typeName + ".instanceof", new Type[1]);
      this.type = type;
   }

   @Override
   public void invokeSafe(final InvokeResult result, final Object... args) {
      result.set(TypeUtils.isInstance(type, args[0]));
   }

   @Override
   protected Member member() {
      return null;
   }
}
