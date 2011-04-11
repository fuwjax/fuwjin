package org.fuwjin.dinah.function;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;

public abstract class AbstractFunction implements Function {
   private final String name;
   private final Type[] argTypes;

   public AbstractFunction(final String name, final Type... argTypes) {
      this.name = name;
      this.argTypes = argTypes;
   }

   protected int argCount() {
      return argTypes.length;
   }

   protected Type argType(final int i) {
      return argTypes()[i];
   }

   protected Type[] argTypes() {
      return argTypes;
   }

   protected boolean isPrivate() {
      return member() == null || Modifier.isPrivate(member().getModifiers());
   }

   protected abstract Member member();

   @Override
   public final String name() {
      return name;
   }
}
