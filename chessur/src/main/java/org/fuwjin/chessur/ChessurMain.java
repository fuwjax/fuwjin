package org.fuwjin.chessur;

import java.util.Map;

public class ChessurMain {
   /**
    * The Catalog/Script runner.
    * @param args the first arg must be a valid absolute/relative/classpath path
    *        to a Grin Catalog. the optional second arg may be a Script to
    *        execute within the Catalog. If the second arg is omitted, the
    *        Catalog will be executed directly.
    * @throws Exception if the catalog cannot load or the script fails
    */
   public static void main(final String... args) throws Exception {
      final CatalogManager manager = new CatalogManager();
      final Catalog cat = manager.loadCat(args[0]);
      Script script = cat;
      if(args.length > 1) {
         script = cat.get(args[1]);
      }
      final Object value = script.exec(System.in, System.out, (Map)System.getProperties());
      System.out.println("\n\n" + script.name() + " returned " + value);
   }
}
