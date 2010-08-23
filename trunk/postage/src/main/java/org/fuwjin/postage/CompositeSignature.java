package org.fuwjin.postage;

import java.util.LinkedList;
import java.util.List;

public class CompositeSignature extends Signature {
   private final List<Signature> signatures;

   public CompositeSignature(final String name) {
      super(name, Object.class, true, Object[].class);
      signatures = new LinkedList<Signature>();
   }

   public void add(final Signature signature) {
      assert name().equals(signature.name());
      signatures.add(signature);
   }

   @Override
   public Class<?>[] params(final int count) {
      for(final Signature signature: signatures) {
         final Class<?>[] ret = signature.params(count);
         if(ret != null) {
            return ret;
         }
      }
      return null;
   }
}
