package org.fuwjin.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Lists all the files that don't start with a ".".
 */
public final class UserFiles implements FilenameFilter {
   @Override
   public boolean accept(final File dir, final String name) {
      return !name.startsWith(".");
   }
}
