package org.fuwjin.dinah;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.fuwjin.dinah.function.AbstractFunction;

/**
 * Buffering function provider to reduce function inspection.
 */
public class CachedFunctionProvider extends AbstractFunctionProvider {
   private final AbstractFunctionProvider[] providers;
   private final Set<String> categories = new HashSet<String>();
   private final Map<String, AbstractFunction> functions = new HashMap<String, AbstractFunction>();

   /**
    * Creates a new instance.
    * @param providers the set of providers
    */
   public CachedFunctionProvider(final AbstractFunctionProvider... providers) {
      this.providers = providers;
   }

   @Override
   public Map<String, AbstractFunction> getFunctions(final String category) {
      if(!categories.contains(category)) {
         for(final AbstractFunctionProvider provider: providers) {
            for(final AbstractFunction func: provider.getFunctions(category).values()) {
               add(functions, func);
            }
         }
         categories.add(category);
      }
      return functions;
   }
}
