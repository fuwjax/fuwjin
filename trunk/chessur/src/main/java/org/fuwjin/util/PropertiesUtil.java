package org.fuwjin.util;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class PropertiesUtil {
   public static final Properties load(final String path) throws IOException {
      final Properties props = new Properties();
      final Reader reader = StreamUtils.reader(path, "UTF-8");
      try {
         props.load(reader);
         return props;
      } finally {
         reader.close();
      }
   }
}
