package org.fuwjin.dinah.provider;

import java.lang.reflect.Type;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.ConstraintBuilder;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.dinah.adapter.AdapterDecorator;
import org.fuwjin.dinah.function.AbstractFunction;

/**
 * A base implementation for FunctionProvider.
 */
public abstract class BaseFunctionProvider extends AdapterDecorator implements FunctionProvider {
   protected BaseFunctionProvider(final Adapter adapter) {
      super(adapter);
   }

   @Override
   public ConstraintBuilder forCategoryName(final Type category, final String simpleName) throws AdaptException {
      return new ConstraintBuilder(this, category, simpleName);
   }

   @Override
   public ConstraintBuilder forName(final String qualifiedName) throws AdaptException {
      return new ConstraintBuilder(this, qualifiedName);
   }

   @Override
   public abstract AbstractFunction getFunction(SignatureConstraint constraint) throws NoSuchFunctionException;
}
