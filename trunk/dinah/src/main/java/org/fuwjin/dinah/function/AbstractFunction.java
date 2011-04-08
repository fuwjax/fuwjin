package org.fuwjin.dinah.function;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.fuwjin.dinah.Function;
import org.fuwjin.util.Adapter;
import org.fuwjin.util.Adapter.AlertTarget;

public abstract class AbstractFunction implements Function {
   protected static class InvokeResult implements AlertTarget {
      private FunctionInvocationException ex;
      private Object value = Adapter.unset();
      private boolean complete = false;
      private boolean success = true;

      public void alert(final boolean complete, final Throwable cause, final String pattern, final Object... args) {
         this.complete = complete;
         ex = new FunctionInvocationException(ex, cause, pattern, args);
         success = false;
      }

      @Override
      public void alert(final String pattern, final Object... args) {
         ex = new FunctionInvocationException(ex, pattern, args);
         success = false;
      }

      public void alert(final Throwable cause, final String pattern, final Object... args) {
         ex = new FunctionInvocationException(ex, cause, pattern, args);
         success = false;
      }

      public boolean isComplete() {
         return complete;
      }

      @Override
      public boolean isSuccess() {
         return success;
      }

      public void reset() {
         success = true;
      }

      public Object resolve() {
         if(isSuccess()) {
            return value;
         }
         return ex;
      }

      public void set(final Object value) {
         complete = true;
         this.value = value;
      }
   }

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

   @Override
   public final Object invoke(final Object... args) {
      final InvokeResult result = new InvokeResult();
      invokeWithResult(result, args);
      return result.resolve();
   }

   protected abstract void invokeWithResult(InvokeResult result, Object[] args);

   protected boolean isPrivate() {
      return member() == null || Modifier.isPrivate(member().getModifiers());
   }

   protected abstract Member member();

   @Override
   public final String name() {
      return name;
   }
}
