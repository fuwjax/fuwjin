package org.fuwjin.dinah.signature;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.SignatureConstraint;

public class CompositeSignature implements FunctionSignature {
   private final SignatureConstraint constraint;
   private final FunctionSignature[] signatures;

   public CompositeSignature(final SignatureConstraint constraint, final FunctionSignature... signatures) {
      this.constraint = constraint;
      this.signatures = signatures;
   }

   @Override
   public Object[] adapt(final Object[] args) throws AdaptException {
      for(final FunctionSignature signature: signatures) {
         try {
            return signature.adapt(args);
         } catch(final AdaptException e) {
            // continue
         }
      }
      throw new AdaptException("No signature for %s met args", this);
   }

   @Override
   public Type argType(final int index) {
      return null;
   }

   @Override
   public boolean canAdapt(final Type[] types) {
      for(final FunctionSignature signature: signatures) {
         if(signature.canAdapt(types)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public Type category() {
      return signatures[0].category();
   }

   @Override
   public FunctionSignature meets(final SignatureConstraint constraint) {
      for(final FunctionSignature signature: signatures) {
         final FunctionSignature sig = signature.meets(constraint);
         if(sig != null) {
            return sig;
         }
      }
      return null;
   }

   @Override
   public String memberName() {
      return constraint.name().substring(constraint.name().lastIndexOf('.' + 1));
   }

   @Override
   public String name() {
      return constraint.name();
   }

   @Override
   public boolean supportsArgs(final int count) {
      for(final FunctionSignature signature: signatures) {
         if(signature.supportsArgs(count)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public String toString() {
      return constraint.toString();
   }
}
