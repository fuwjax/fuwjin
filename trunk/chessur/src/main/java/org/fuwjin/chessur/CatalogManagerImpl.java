package org.fuwjin.chessur;

import static org.fuwjin.chessur.generated.GrinParser.interpret;
import static org.fuwjin.util.StreamUtils.inputStream;
import static org.fuwjin.util.StreamUtils.readAll;
import static org.fuwjin.util.StreamUtils.reader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import org.fuwjin.dinah.Adapter;
import org.fuwjin.dinah.adapter.StandardAdapter;
import org.fuwjin.dinah.provider.BaseFunctionProvider;
import org.fuwjin.dinah.provider.CompositeFunctionProvider;
import org.fuwjin.dinah.provider.FunctionProviderDecorator;

/**
 * Manages catalogs.
 */
public class CatalogManagerImpl extends FunctionProviderDecorator implements CatalogManager {
   private final ConcurrentMap<String, Catalog> catalogs = new ConcurrentHashMap<String, Catalog>();

   /**
    * Creates a new instance.
    */
   public CatalogManagerImpl() {
      this(new CompositeFunctionProvider());
   }

   /**
    * Creates a new instance backed by the adapter.
    * @param adapter the type adapter
    */
   public CatalogManagerImpl(final Adapter adapter) {
      this(new CompositeFunctionProvider(adapter));
   }

   /**
    * Creates a new instance backed by the function provider.
    * @param provider the function provider
    */
   public CatalogManagerImpl(final BaseFunctionProvider provider) {
      super(provider);
   }

   public CatalogManagerImpl(final ClassLoader loader) {
      this(new StandardAdapter(loader));
   }

   /**
    * Loads a catalog from a file with a default charset.
    * @param file the catalog file
    * @return the new collection
    * @throws ExecutionException if it fails
    * @throws IOException if I/O fails
    */
   public Catalog loadCat(final File file) throws ExecutionException, IOException {
      return loadCat(file, "UTF-8");
   }

   /**
    * Loads a catalog from a file with a specific charset.
    * @param file the catalog file
    * @param charset the character encoding for the file
    * @return the new collection
    * @throws ExecutionException if it fails
    * @throws IOException if I/O fails
    */
   public Catalog loadCat(final File file, final String charset) throws ExecutionException, IOException {
      return loadCat(file.getAbsolutePath(), reader(new FileInputStream(file), charset));
   }

   /**
    * Loads a catalog from a file.
    * @param path the path to the catalog file
    * @return the new collection
    * @throws ExecutionException if it fails
    * @throws IOException if I/O fails
    */
   @Override
   public Catalog loadCat(final String path) throws ExecutionException, IOException {
      return loadCat(path, "UTF-8");
   }

   /**
    * Loads a catalog from a reader. This method should only be used when
    * strictly necessary, preferring other loadCat methods instead.
    * @param name the logical name of the catalog, usually the file name
    * @param reader the input reader
    * @return the catalog
    * @throws IOException if I/O fails
    * @throws ExecutionException if the parse fails
    */
   public Catalog loadCat(final String name, final Reader reader) throws IOException, ExecutionException {
      Catalog cat = catalogs.get(name);
      if(cat == null) {
         final Map<String, Object> map = new HashMap<String, Object>();
         map.put("name", name);
         map.put("manager", this);
         try {
            cat = (Catalog)interpret(readAll(reader), new StringBuilder(), new StringBuilder(), map);
         } catch(final IOException e) {
            throw e;
         } catch(final Exception e) {
            throw new ExecutionException("could not execute catalog " + name, e);
         }
         final Catalog old = catalogs.putIfAbsent(name, cat);
         if(old != null) {
            cat = old;
         }
      }
      return cat;
   }

   /**
    * Loads a catalog from a file with a specific charset.
    * @param path the catalog file
    * @param charset the character encoding for the file
    * @return the new collection
    * @throws ExecutionException if it fails
    * @throws IOException if I/O fails
    */
   public Catalog loadCat(final String path, final String charset) throws ExecutionException, IOException {
      return loadCat(path, reader(inputStream(path), charset));
   }
}
