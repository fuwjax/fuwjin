package org.fuwjin.postage.category;

import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.function.ConstantFunction;

public class VoidCategory extends AbstractCategory {
   public VoidCategory() {
      super("void");
   }

   @Override
   protected Function newFunction(final String name) {
      return new ConstantFunction(name, new Failure("Void messages always fail"));
   }
}
