package org.fuwjin.dinah;

public interface SignatureConstraint {
   public String category();

   public String name();

   boolean matches(FunctionSignature signature);
}
