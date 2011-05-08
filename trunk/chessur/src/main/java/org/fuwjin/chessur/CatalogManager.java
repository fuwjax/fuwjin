package org.fuwjin.chessur;

import static org.fuwjin.chessur.generated.ChessurInterpreter.interpret;
import static org.fuwjin.util.StreamUtils.inputStream;
import static org.fuwjin.util.StreamUtils.readAll;
import static org.fuwjin.util.StreamUtils.reader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.fuwjin.chessur.generated.ChessurInterpreter.ChessurException;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.ReflectiveFunctionProvider;

/**
 * Manages catalogs
 */
public class CatalogManager {
   private final ConcurrentMap<String, Catalog> catalogs = new ConcurrentHashMap<String, Catalog>();
   private final FunctionProvider provider;

   /**
    * Creates a new instance.
    */
   public CatalogManager() {
      this(new ReflectiveFunctionProvider());
   }

   /**
    * Creates a new instance backed by the function provider.
    * @param provider
    */
   public CatalogManager(final FunctionProvider provider) {
      this.provider = provider;
   }

   /**
    * Loads a catalog from a file.
    * @param file the catalog file
    * @return the new collection
    * @throws ChessurException if it fails
    * @throws IOException
    */
   public Catalog loadCat(final File file) throws ChessurException, IOException {
      return loadCat(file.getAbsolutePath(), new FileReader(file));
   }

   /**
    * Loads a catalog from a file.
    * @param path the path to the catalog file
    * @return the new collection
    * @throws ChessurException if it fails
    * @throws IOException
    */
   public Catalog loadCat(final String path) throws ChessurException, IOException {
      return loadCat(path, reader(inputStream(path), "UTF-8"));
   }

   protected Catalog loadCat(final String name, final Reader reader) throws IOException, ChessurException {
      Catalog cat = catalogs.get(name);
      if(cat == null) {
         final Map<String, Object> map = new HashMap<String, Object>();
         map.put("postage", provider);
         map.put("name", name);
         map.put("manager", this);
         cat = (Catalog)interpret(readAll(reader), new StringBuilder(), map);
         final Catalog old = catalogs.putIfAbsent(name, cat);
         if(old != null) {
            cat = old;
         }
      }
      return cat;
   }
}
