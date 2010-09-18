package org.fuwjin.pogo.state;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.pogo.CodePointStreamFactory;
import org.fuwjin.pogo.position.IntRangeSet;
import org.fuwjin.postage.Failure;

public class ParseFailure {
   private static class FailureTrace {
      private final String name;
      private final AbstractPosition position;
      private final int level;

      public FailureTrace(final int level, final String name, final AbstractPosition position) {
         this.level = level;
         this.name = name;
         this.position = position;
      }

      @Override
      public String toString() {
         return "  in " + name + position;
      }
   }

   private final IntRangeSet set = new IntRangeSet();
   private AbstractPosition current;
   private final List<FailureTrace> stack = new LinkedList<FailureTrace>();
   private String message;
   private Failure cause;

   public ParseException exception() {
      if(message == null) {
         return new ParseException(current, "failed test: '"
               + CodePointStreamFactory.toString(((ParsePosition)current).codePoint()) + "' expecting [" + set + "]",
               stack);
      }
      if(cause == null) {
         return new ParseException(current, message, stack);
      }
      return new ParseException(current, message, cause.exception(), stack);
   }

   public void fail(final AbstractPosition position, final int start, final int end) {
      if(current == null) {
         current = position;
      } else if(position.index() > current.index()) {
         fail(position, null, null);
      } else if(current.index() > position.index()) {
         return;
      }
      set.unionRange(start, end);
   }

   public void fail(final AbstractPosition position, final String message, final Failure cause) {
      if(current == null || position.index() >= current.index()) {
         current = position;
         set.clear();
         stack.clear();
         this.message = message;
         this.cause = cause;
      }
   }

   public void failStack(final int level, final String name, final AbstractPosition position) {
      if(stack.size() == 0 || stack.get(stack.size() - 1).level > level) {
         stack.add(new FailureTrace(level, name, position));
      }
   }
}
