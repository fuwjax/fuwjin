package org.fuwjin.postage.category;

import org.fuwjin.postage.CompositeFailure;
import org.fuwjin.postage.CompositeSignature;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.function.CompositeFunction;

public class GlobalCategory extends AbstractCategory {
   public GlobalCategory(final Postage postage) {
      super(null, postage);
   }

   @Override
   public Object invokeFallThrough(final CompositeSignature signature, final CompositeFailure current,
         final Object... args) {
      return current;
   }

   @Override
   protected CompositeFunction newFunction(final String name) {
      return new CompositeFunction(name, this);
   }
}
