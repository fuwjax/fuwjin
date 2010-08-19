package org.fuwjin.io;

import static org.fuwjin.io.PogoFailure.FailureState.FAILURE;
import static org.fuwjin.io.PogoFailure.FailureState.NEUTRAL;
import static org.fuwjin.io.PogoFailure.FailureState.SUCCESS;

public class PogoFailure {
   enum FailureState {
      SUCCESS, NEUTRAL, FAILURE;
   }

   private static class Reason {
      private final Reason next;
      private final String reason;

      private Reason(final Reason next, final String reason, final Throwable cause) {
         this.next = next;
         this.reason = reason;
      }

      @Override
      public String toString() {
         return reason + (next == null ? "" : "\n" + next);
      }
   }

   private Reason reasons;
   private IntSet chars;
   private FailureState state;
   private final InternalPosition position;
   private PogoFailure forward;

   public PogoFailure(final InternalPosition position) {
      this.position = position;
   }

   public void addFailure(final int low, final int high) {
      state = FAILURE;
      if(chars == null) {
         chars = new IntSet();
      }
      chars.unionRange(low, high);
   }

   public void addFailure(final PogoFailure failure) {
      state = FAILURE;
      assert !isAfter(failure): this + " > " + failure;
      if(failure != this && failure.isAfter(forward)) {
         forward = failure;
      }
   }

   public void addFailure(final String reason, final Throwable cause) {
      state = FAILURE;
      reasons = new Reason(reasons, reason, cause);
   }

   public void clear() {
      state = SUCCESS;
      reasons = null;
      chars = null;
      if(forward != null) {
         forward.clear();
         forward = null;
      }
   }

   public PogoException exception() {
      if(forward != null) {
         return forward.exception();
      }
      return new PogoException(this);
   }

   private boolean isAfter(final PogoFailure failure) {
      if(failure == null) {
         return true;
      }
      return position.isAfter(failure.position);
   }

   public boolean isSuccess() {
      return state != FAILURE;
   }

   public void neutral() {
      state = NEUTRAL;
   }

   public String toMessage() {
      final StringBuilder builder = new StringBuilder();
      builder.append("Error parsing ").append(position.toMessage());
      builder.append(": failed test: '");
      position.append(builder);
      builder.append("' expecting '").append(chars).append("'");
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
