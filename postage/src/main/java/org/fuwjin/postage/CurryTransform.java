package org.fuwjin.postage;

import org.fuwjin.postage.function.CurriedTarget;

public class CurryTransform implements TargetTransform {
   private final Object[] args;

   public CurryTransform(final Object[] args) {
      this.args = args;
   }

   @Override
   public FunctionTarget transform(final FunctionTarget target) {
      return CurriedTarget.curry(target, args);
   }
}
