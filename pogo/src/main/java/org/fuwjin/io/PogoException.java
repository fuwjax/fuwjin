package org.fuwjin.io;

public class PogoException extends Exception {
   private static final long serialVersionUID = 1L;

   public PogoException(final PogoFailure failure) {
      super(failure.toMessage());
   }
}
