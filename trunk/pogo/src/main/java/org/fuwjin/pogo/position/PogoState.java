package org.fuwjin.pogo.position;

import org.fuwjin.pogo.Memo;
import org.fuwjin.pogo.PogoException;
import org.fuwjin.postage.Failure;

public class PogoState {
   private static class Reason {
      private final Reason next;
      private final String reason;
      private final Failure cause;
      private Throwable ex;

      private Reason(final Reason next, final String reason, final Failure cause) {
         this.next = next;
         this.reason = reason;
         this.cause = cause;
      }

      private Reason(final Reason next, final String reason, final Throwable cause) {
         this.next = next;
         this.reason = reason;
         this.cause = null;
         ex = cause;
      }

      public Throwable cause() {
         if(ex == null) {
            ex = cause.exception();
         }
         return ex;
      }

      @Override
      public String toString() {
         return reason + (next == null ? "" : "\n" + next);
      }
   }

   private Reason reasons;
   private final IntRangeSet chars = new IntRangeSet();
   private InternalPosition position;
   private boolean success = true;
   private Memo current;

   public PogoState(final InternalPosition initial) {
      position = initial;
   }

   public void addFailure(final InternalPosition failPos, final int low, final int high) {
      if(fail(failPos)) {
         chars.unionRange(low, high);
      }
   }

   public void addFailure(final InternalPosition failPos, final String reason, final Failure cause) {
      if(fail(failPos)) {
         reasons = new Reason(reasons, reason, cause);
      }
   }

   public void addFailure(final InternalPosition failPos, final String reason, final Throwable cause) {
      if(fail(failPos)) {
         reasons = new Reason(reasons, reason, cause);
      }
   }

   public PogoException exception() {
      if(reasons != null) {
         return new PogoException(toMessage(), reasons.cause());
      }
      return new PogoException(toMessage());
   }

   private boolean fail(final InternalPosition position) {
      success = false;
      if(position.isAfter(this.position)) {
         chars.clear();
         reasons = null;
         this.position = position;
      }
      return !this.position.isAfter(position);
   }

   public boolean isSuccess() {
      return success;
   }

   public Memo memo() {
      return current;
   }

   public void newMemo(final InternalPosition position, final String name, final Object value) {
      current = new Memo(position, name, value);
   }

   public Memo releaseMemo(final Memo newMemo) {
      final Memo memo = current;
      current = newMemo;
      return memo;
   }

   public void success() {
      success = true;
   }

   public String toMessage() {
      final StringBuilder builder = new StringBuilder();
      builder.append("Error parsing ").append(current.name()).append(position.toMessage());
      if(reasons != null) {
         builder.append(reasons);
      } else {
         builder.append(": failed test: '");
         position.append(builder);
         builder.append("' expecting '").append(chars).append("'");
      }
      return builder.toString();
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append(position);
      if(chars != null) {
         builder.append(" [").append(chars).append(']');
      }
      if(reasons != null) {
         builder.append('\n').append(reasons);
      }
      return builder.toString();
   }
}
