package org.fuwjin.postage;

import java.util.ArrayList;
import java.util.List;

public class CompositeFailure extends Failure {
   private static final long serialVersionUID = 1L;
   private final List<Failure> failures = new ArrayList<Failure>();

   public CompositeFailure(final String pattern, final Object... args) {
      super(pattern, args);
   }

   public void addFailure(final Failure failure) {
      failures.add(failure);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CompositeFailure o = (CompositeFailure)obj;
         return getClass().equals(o.getClass()) && failures.equals(o.failures);
      } catch(final RuntimeException e) {
         return false;
      }
   }
}
