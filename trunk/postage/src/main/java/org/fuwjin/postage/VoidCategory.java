package org.fuwjin.postage;

public class VoidCategory extends AbstractCategory {
   public VoidCategory() {
      super("void");
   }

   @Override
   protected Function newFunction(final String name) {
      return new ConstantFunction(name, new Failure("Void messages always fail"));
   }
}
