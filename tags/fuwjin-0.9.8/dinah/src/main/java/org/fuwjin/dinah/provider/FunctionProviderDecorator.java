package org.fuwjin.dinah.provider;

import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.dinah.function.AbstractFunction;

/**
 * A base implementation for FunctionProvider.
 */
public abstract class FunctionProviderDecorator extends BaseFunctionProvider {
   private final BaseFunctionProvider provider;

   protected FunctionProviderDecorator(final BaseFunctionProvider provider) {
      super(provider);
      this.provider = provider;
   }

   @Override
   public AbstractFunction getFunction(final SignatureConstraint constraint) throws NoSuchFunctionException {
      return provider.getFunction(constraint);
   }
}
