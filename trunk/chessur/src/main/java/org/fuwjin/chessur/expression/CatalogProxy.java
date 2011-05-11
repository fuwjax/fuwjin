package org.fuwjin.chessur.expression;

import java.util.Iterator;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.Module;
import org.fuwjin.chessur.Script;

/**
 * Represents a loaded module in a Catalog.
 */
public class CatalogProxy implements Module {
   private final Catalog module;
   private final String name;

   /**
    * Creates a new instance.
    * @param name the loaded name
    * @param module the catalog
    */
   public CatalogProxy(final String name, final Catalog module) {
      this.name = name;
      this.module = module;
   }

   /**
    * Returns the catalog.
    * @return the catalog
    */
   public Catalog catalog() {
      return module;
   }

   @Override
   public Script get(final String script) {
      return new ScriptProxy(this, (Executable)module.get(script));
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public Iterable<? extends Script> scripts() {
      final Iterable<? extends Script> scripts = module.scripts();
      return new Iterable<ScriptProxy>() {
         @Override
         public Iterator<ScriptProxy> iterator() {
            final Iterator<? extends Script> iter = scripts.iterator();
            return new Iterator<ScriptProxy>() {
               @Override
               public boolean hasNext() {
                  return iter.hasNext();
               }

               @Override
               public ScriptProxy next() {
                  return new ScriptProxy(CatalogProxy.this, (Executable)iter.next());
               }

               @Override
               public void remove() {
                  iter.remove();
               }
            };
         }
      };
   }

   @Override
   public String source() {
      return module.source();
   }
}
