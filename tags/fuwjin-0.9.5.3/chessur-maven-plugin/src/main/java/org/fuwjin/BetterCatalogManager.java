package org.fuwjin;

import static org.fuwjin.util.StreamUtils.reader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManager;
import org.fuwjin.chessur.generated.ChessurInterpreter.ChessurException;

public class BetterCatalogManager extends CatalogManager {
   /**
    * Creates an input stream from the classpath, if possible, otherwise from
    * the file system.
    * @param path the path to the file
    * @return the new input stream
    * @throws FileNotFoundException if the path does not point to a file
    */
   public static InputStream inputStream(final String path) throws FileNotFoundException {
      InputStream stream = ClassLoader.getSystemResourceAsStream(path);
      if(stream == null) {
         stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
         if(stream == null) {
            stream = new FileInputStream(path);
         }
      }
      return stream;
   }

   @Override
   public Catalog loadCat(final String path) throws ChessurException, IOException {
      return loadCat(path, reader(inputStream(path), "UTF-8"));
   }
}
