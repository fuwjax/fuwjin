package org.fuwjin.buffer;

public class Invocation {
   private final Object[] args;

   public Invocation(final Object[] args, final Object ret, final Throwable ex) {
      this.args = args;
   }

   public Object arg(final int index) {
      return args[index];
   }

   public int argCount() {
      return args.length;
   }
}
