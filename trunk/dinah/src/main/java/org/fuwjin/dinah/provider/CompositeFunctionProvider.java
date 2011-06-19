package org.fuwjin.dinah.provider;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.ConstraintBuilder;
import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.dinah.adapter.StandardAdapter;
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.CompositeFunction;

/**
 * Buffering function provider to reduce function inspection.
 */
public class CompositeFunctionProvider extends BaseFunctionProvider {
   private final BaseFunctionProvider[] providers;

   public CompositeFunctionProvider() {
      this(new StandardAdapter());
   }

   public CompositeFunctionProvider(final Adapter adapter) {
      this(adapter, new VirtualArrayFunctionProvider(adapter), new ReflectiveFunctionProvider(adapter));
   }

   /**
    * Creates a new instance.
    * @param providers the set of providers
    */
   public CompositeFunctionProvider(final Adapter adapter, final BaseFunctionProvider... providers) {
      super(adapter);
      this.providers = providers;
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
   public AbstractFunction getFunction(final SignatureConstraint constraint) throws NoSuchFunctionException {
      final List<AbstractFunction> functions = new ArrayList<AbstractFunction>();
      for(final BaseFunctionProvider provider: providers) {
         try {
            functions.add(provider.getFunction(constraint));
         } catch(final NoSuchFunctionException e) {
            // continue
         }
      }
      return CompositeFunction.merge(constraint, functions);
   }
}
