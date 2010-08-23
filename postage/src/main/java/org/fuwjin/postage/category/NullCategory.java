package org.fuwjin.postage.category;

import org.fuwjin.postage.CompositeFailure;
import org.fuwjin.postage.CompositeSignature;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.function.AbstractFunction;
import org.fuwjin.postage.function.CompositeFunction;

public class NullCategory extends AbstractCategory {
   public NullCategory(final Postage postage) {
      super("null", postage);
      addFunction(new AbstractFunction("instanceof", boolean.class, false, Object.class) {
         @Override
         public Object tryInvoke(final Object... args) {
            return args[0] == null;
         }
      });
   }

   @Override
   public Object invokeFallThrough(final CompositeSignature signature, final CompositeFailure current,
         final Object... args) {
      if(current.isEmpty()) {
         return null;
      }
      return super.invokeFallThrough(signature, current, args);
   }

   @Override
   protected CompositeFunction newFunction(final String name) {
      return new CompositeFunction(name, this);
   }
}
