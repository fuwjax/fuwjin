package org.fuwjin.dinah;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.FunctionProvider.NoSuchFunctionException;
import org.fuwjin.dinah.signature.ArgCountSignature;
import org.fuwjin.dinah.signature.NameSignatureConstraint;
import org.fuwjin.dinah.signature.TypedArgsSignature;

public class ConstraintBuilder {
   private SignatureConstraint constraint;
   private List<Type> types = null;
   private final FunctionProvider adapter;

   public ConstraintBuilder(final FunctionProvider adapter, final String qualifiedName) throws AdaptException {
      this.adapter = adapter;
      final String categoryName = qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));
      constraint = new NameSignatureConstraint(categoryName, qualifiedName);
   }

   public ConstraintBuilder(final FunctionProvider adapter, final Type category, final String simpleName)
         throws AdaptException {
      this.adapter = adapter;
      final String categoryName = adapter.adapt(category, String.class);
      constraint = new NameSignatureConstraint(categoryName, categoryName + "." + simpleName);
   }

   public SignatureConstraint constraint() {
      if(types != null) {
         final Type[] typeArray = types.toArray(new Type[types.size()]);
         constraint = new TypedArgsSignature(constraint, typeArray);
      }
      return constraint;
   }

   public Function function() throws NoSuchFunctionException {
      return adapter.getFunction(constraint());
   }

   public ConstraintBuilder withArgCount(final int count) {
      constraint = new ArgCountSignature(constraint, count);
      return this;
   }

   public ConstraintBuilder withArgs(final String... types) throws AdaptException {
      return this;
   }

   public ConstraintBuilder withNextArg(final String type) throws AdaptException {
      return withNextTypedArg(adapter.adapt(type, Type.class));
   }

   public ConstraintBuilder withNextTypedArg(final Type type) {
      if(types == null) {
         types = new ArrayList<Type>();
      }
      types.add(type);
      return this;
   }

   public ConstraintBuilder withTypedArgs(final Type... types) {
      constraint = new TypedArgsSignature(constraint, types);
      return this;
   }
}
