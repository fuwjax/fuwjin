package org.fuwjin.complier;

import static java.util.Collections.singleton;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject.Kind;

/**
 * A ClassLoader which compiles source code at runtime in memory.
 */
public class RuntimeClassLoader extends ClassLoader {
   private final ConcurrentMap<String, BufferedFileObject> files = new ConcurrentHashMap<String, BufferedFileObject>();
   private final ForwardingJavaFileManager<StandardJavaFileManager> manager =
         new ForwardingJavaFileManager<StandardJavaFileManager>(getStandardFileManager()) {
            @Override
            public JavaFileObject getJavaFileForOutput(final Location location, final String className,
                  final Kind kind, final FileObject sibling) throws IOException {
               return getFile(className);
            }
         };

   /**
    * Compiles the {@code source} java code into the class {@code name}.
    * @param name the fully qualified class name
    * @param source the java source code
    * @return true if the source code compiled, false otherwise
    */
   public boolean compile(final String name, final String source) {
      final Set<BufferedFileObject> compUnit = singleton(new BufferedFileObject(name, source));
      return getSystemJavaCompiler().getTask(null, manager, null, null, null, compUnit).call();
   }

   private StandardJavaFileManager getStandardFileManager() {
      return getSystemJavaCompiler().getStandardFileManager(null, null, null);
   }

   @Override
   protected Class<?> findClass(final String name) throws ClassNotFoundException {
      final byte[] b = files.get(name).getBytes();
      return defineClass(name, b, 0, b.length);
   }

   /**
    * Returns the file object for the {@code className}.
    * @param className the name of the file object
    * @return the file object
    */
   JavaFileObject getFile(final String className) {
      final BufferedFileObject file = new BufferedFileObject(className);
      files.put(className, file);
      return file;
   }
}
