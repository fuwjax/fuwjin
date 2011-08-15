package org.fuwjin.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public abstract class AssertThat {
   public class Assertion<T extends Throwable> {
      private final T cause;

      public Assertion(final T cause) {
         this.cause = cause;
      }

      public Assertion<T> withMessage(final String message) {
         assertEquals(message, cause.getMessage());
         return this;
      }
   }

   public abstract void when() throws Throwable;

   public <X extends Throwable>Assertion<X> willThrow(final Class<X> exception) {
      X cause = null;
      try {
         when();
      } catch(final Throwable e) {
         assertThat(e, is(exception));
         cause = exception.cast(e);
      }
      assertNotNull("Operation completed successfully", cause);
      return new Assertion<X>(cause);
   }
}
