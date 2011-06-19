package org.fuwjin.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManager;
import org.fuwjin.dinah.SignatureConstraint;
import org.fuwjin.dinah.function.AbstractFunction;
import org.fuwjin.dinah.function.CompositeFunction;
import org.fuwjin.dinah.provider.BaseFunctionProvider;

public class VirtualClassFunctionProvider extends BaseFunctionProvider {
   private final File sourcePath;
   private final Catalog sourceParser;

   protected VirtualClassFunctionProvider(final CatalogManager manager, final File sourcePath)
         throws ExecutionException, IOException {
      super(manager);
      this.sourcePath = sourcePath;
      sourceParser = manager.loadCat("org/fuwjin/chessur/compiler/JavaSignatureParser.cat");
   }

   @Override
   public AbstractFunction getFunction(final SignatureConstraint constraint) throws NoSuchFunctionException {
      try {
         final List<AbstractFunction> functions = new ArrayList<AbstractFunction>();
         final VirtualClass cls = loadClass(constraint.category());
         for(final AbstractFunction function: cls.functions()) {
            if(constraint.matches(function.signature())) {
               functions.add(function);
            }
         }
         return CompositeFunction.merge(constraint, functions);
      } catch(final AdaptException e) {
         throw new NoSuchFunctionException(e, "Could not load virtual class %s", constraint.category());
      }
   }

   public VirtualClass loadClass(final String category) throws AdaptException {
      final File file = new File(sourcePath, category.replace('.', '/') + ".java");
      try {
         final FileInputStream input = new FileInputStream(file);
         return (VirtualClass)sourceParser.exec(input, System.out);
      } catch(final Exception e) {
         throw new AdaptException(e, "Could not open %s", file);
      }
   }
}
