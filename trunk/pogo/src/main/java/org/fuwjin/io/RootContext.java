package org.fuwjin.io;

import static java.lang.Math.max;

import java.text.ParseException;

/**
 * A base PogoContext for managing parse state.
 */
public abstract class RootContext implements PogoContext {
   private static final String FAILED_PARSE = "Failed parse"; //$NON-NLS-1$
   private boolean success;
   private Object object;
   private final StringBuilder failure = new StringBuilder(FAILED_PARSE);
   private int failurePosition = -1;
   /**
    * If true, generated parse errors will be much more descriptive.
    */
   public static boolean DEBUG = false;

   /**
    * Creates a new instance.
    * @param object the initial context object, may be null
    */
   protected RootContext(final Object object) {
      this.object = object;
      success = true;
      failurePosition = 0;
   }

   @Override
   public void assertSuccess() throws ParseException {
      if(!success) {
         throw new ParseException(failure.toString(), failurePosition);
      }
   }

   @Override
   public Object get() {
      return object;
   }

   public abstract boolean hasRemaining();

   @Override
   public boolean isSuccess() {
      return success;
   }

   @Override
   public String match() {
      return substring(0);
   }

   /**
    * Creates a new child context under this root.
    * @return the new child context
    */
   public abstract PogoContext newChild();

   @Override
   public PogoContext newChild(final Object newObject, final boolean newSuccess, final String failureReason) {
      return newChild().set(newObject, newSuccess, failureReason);
   }

   @Override
   public PogoContext set(final Object object, final boolean success, final String failureReason) {
      this.object = object;
      success(success, failureReason);
      return this;
   }

   /**
    * Returns the substring of this context from start to the current position.
    * @param start the start of the substring
    * @return the substring
    */
   public abstract String substring(int start);

   @Override
   public boolean success(final boolean state, final String failureReason) {
      success = state;
      if(DEBUG) {
         if(success) {
            if(position() > failurePosition) {
               failure.setLength(0);
               failurePosition = position();
            }
         } else {
            if(failure.length() == 0) {
               failure.append('~').append(failurePosition).append('@').append(substring(max(0, failurePosition - 10)));
            }
            if(position() == failurePosition) {
               failure.append(':').append(failureReason);
            }
         }
      } else if(success && position() > failurePosition) {
         failurePosition = position();
      }
      return success;
   }
}
