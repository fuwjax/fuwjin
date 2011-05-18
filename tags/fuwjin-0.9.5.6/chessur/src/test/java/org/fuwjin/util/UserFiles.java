package org.fuwjin.util;

import java.io.File;
import java.io.FilenameFilter;

public final class UserFiles implements FilenameFilter {
   @Override
   public boolean accept(final File dir, final String name) {
      return !name.startsWith(".");
   }
}