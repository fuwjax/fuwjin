package org.fuwjin.chessur;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.fuwjin.dinah.FunctionProvider;

public interface CatalogManager extends FunctionProvider {
   Catalog loadCat(String path) throws ExecutionException, IOException;
}
