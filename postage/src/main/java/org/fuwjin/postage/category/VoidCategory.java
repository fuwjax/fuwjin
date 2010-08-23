package org.fuwjin.postage.category;

import org.fuwjin.postage.Postage;
import org.fuwjin.postage.function.CompositeFunction;

public class VoidCategory extends AbstractCategory {
   public VoidCategory(final Postage postage) {
      super("void", postage);
   }

   public VoidCategory(final String name, final Postage postage) {
      super(name, postage);
   }

   @Override
   protected CompositeFunction newFunction(final String name) {
      return new CompositeFunction(name, this);
   }
}
