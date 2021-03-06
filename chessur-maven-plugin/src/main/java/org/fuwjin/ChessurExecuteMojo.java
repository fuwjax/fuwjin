package org.fuwjin;

import static org.fuwjin.util.StreamUtils.reader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which runs a Grin Catalog against every file in a fileset.
 * @goal exec
 * @requiresDependencyResolution compile
 * @phase generate-sources
 */
public class ChessurExecuteMojo extends AbstractMojo {
   private final class MavenLogger extends Writer {
      @Override
      public void close() throws IOException {
         // do nothing
      }

      @Override
      public void flush() throws IOException {
         // do nothing
      }

      @Override
      public void write(final char[] chars, final int offset, final int length) throws IOException {
         getLog().info(new String(chars, offset, length));
      }
   }

   /**
    * File encoding for the input files.
    * @parameter expression="${project.build.sourceEncoding}"
    *            default-value="UTF-8"
    */
   private String fileEncoding;
   /**
    * Location for the execution input.
    * @parameter expression="${project.build.sourceDirectory}/../grin"
    */
   private File sourceDirectory;
   /**
    * Location for the generated output.
    * @parameter expression="${project.build.directory}/generated-sources/grin"
    * @required
    */
   private File outputDirectory;
   /**
    * Location of the catalog file. May be relative to the classpath or the
    * filesystem.
    * @parameter expression="${chessur.build.catFile}"
    *            default-value="org/fuwjin/chessur/compiler/GrinCompiler.cat"
    */
   private String catFile;
   /**
    * Extension of source files.
    * @parameter expression="${chessur.build.sourceExtension}"
    *            default-value=".cat"
    */
   private String sourceExtension;
   /**
    * Extension of destination files.
    * @parameter expression="${chessur.build.outputExtension}"
    *            default-value=".java"
    */
   private String outputExtension;
   /**
    * The classpath elements of the project.
    * @parameter expression="${project.runtimeClasspathElements}"
    * @required
    * @readonly
    */
   private List<String> classpath;
   /**
    * @parameter expression="${project}"
    * @required
    * @readonly
    */
   private MavenProject project;
   private ClassLoader loader;
   private final MavenLogger mavenLogger = new MavenLogger();

   /**
    * Creates a new instance.
    */
   public ChessurExecuteMojo() {
      // normal constructor
   }

   /**
    * Creates a new instance. Reserved for testing.
    * @param catFile the catalog file path
    * @param source the input file
    * @param output the output file
    */
   public ChessurExecuteMojo(final String catFile, final File source, final File output) {
      this.catFile = catFile;
      sourceDirectory = source;
      outputDirectory = output;
      fileEncoding = "UTF-8";
      sourceExtension = ".cat";
      outputExtension = ".java";
   }

   @Override
   public void execute() throws MojoExecutionException, MojoFailureException {
      try {
         getLog().info("Transforming " + sourceDirectory + " to " + outputDirectory);
         final ScriptEngineManager engines = new ScriptEngineManager();
         final ScriptEngine engine = engines.getEngineByName("chessur");
         final CompiledScript cat = ((Compilable)engine).compile(reader(catFile, "UTF-8"));
         loader = loader();
         transform(cat, sourceDirectory, null);
         if(project != null) {
            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
         }
      } catch(final IOException e) {
         throw new MojoExecutionException("Error loading catalog " + catFile, e);
      } catch(final Exception e) {
         throw new MojoExecutionException("Syntax error in " + catFile, e);
      }
   }

   ClassLoader loader() throws MalformedURLException {
      if(classpath == null) {
         return Thread.currentThread().getContextClassLoader();
      }
      getLog().info("Using project classpath: " + classpath);
      final URL[] urls = new URL[classpath.size()];
      int index = 0;
      for(final String element: classpath) {
         urls[index++] = new File(element).toURI().toURL();
      }
      return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
   }

   String path(final File source) {
      try {
         return source.getCanonicalPath();
      } catch(final IOException e) {
         return source.getAbsolutePath();
      }
   }

   private String stripExt(final String path) {
      return path.substring(0, path.length() - sourceExtension.length());
   }

   private void transform(final CompiledScript cat, final File source, final File dest) throws MojoExecutionException {
      if(source.isDirectory()) {
         for(final File file: source.listFiles()) {
            transform(cat, file, dest == null ? new File("") : new File(dest, source.getName()));
         }
      } else {
         if(!source.getName().endsWith(sourceExtension)) {
            return;
         }
         final File folder = new File(outputDirectory, dest.getPath());
         final String name = stripExt(source.getName());
         final File target = new File(folder, name + outputExtension);
         try {
            final InputStream input = new FileInputStream(source);
            try {
               final Reader reader = new InputStreamReader(input, fileEncoding);
               try {
                  folder.mkdirs();
                  final FileOutputStream output = new FileOutputStream(target);
                  try {
                     final Writer writer = new OutputStreamWriter(output, fileEncoding);
                     try {
                        final Bindings map = cat.getEngine().createBindings();
                        map.put("folder", dest);
                        map.put("package", dest.getPath().substring(1).replace("/", ".").replace("\\", "."));
                        map.put("target", target);
                        map.put("name", name);
                        map.put("loader", loader);
                        final ScriptContext context = new SimpleScriptContext();
                        context.setReader(reader);
                        context.setWriter(writer);
                        context.setErrorWriter(mavenLogger);
                        context.setBindings(map, ScriptContext.ENGINE_SCOPE);
                        cat.eval(context);
                     } catch(final ScriptException e) {
                        throw new MojoExecutionException("Could not transform from " + path(source) + " to "
                              + path(target), e);
                     } finally {
                        writer.close();
                     }
                  } finally {
                     output.close();
                  }
               } finally {
                  reader.close();
               }
            } finally {
               input.close();
            }
         } catch(final IOException e) {
            throw new MojoExecutionException("File system error during transform from " + path(source) + " to "
                  + path(target), e);
         }
      }
   }
}
