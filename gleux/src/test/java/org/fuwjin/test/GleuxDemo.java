package org.fuwjin.test;

import static java.util.Collections.singletonMap;
import static org.fuwjin.gleux.Gleux.newGleux;
import static org.fuwjin.gleux.InStream.STDIN;
import static org.fuwjin.gleux.InStream.stream;
import static org.fuwjin.gleux.OutStream.STDOUT;
import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StreamUtils.writer;
import static org.fuwjin.util.StringUtils.readAll;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.fuwjin.gleux.Gleux;
import org.fuwjin.gleux.GleuxInterpreter.GleuxException;
import org.fuwjin.gleux.InStream;
import org.fuwjin.gleux.OutStream;
import org.junit.Test;

/**
 * Demos parsing a file.
 */
public class GleuxDemo {
   public static void echo(final String message) {
      System.out.println("echoing " + message);
   }

   @Test
   public void demoForSatish() throws IOException, GleuxException {
      final Gleux parser = newGleux(readAll(reader("gleux.parse.gleux", "UTF-8")));
      final Gleux gleux = (Gleux)parser.transform(InStream.stream(reader("satish.gleux", "UTF-8")), OutStream.STDOUT,
            Collections.<String, Object> singletonMap("postage", new ReflectiveFunctionProvider()));
      final InStream input = STDIN;
      final OutStream output = STDOUT;
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("list", Arrays.asList(1, 2, 3));
      final Object result = gleux.transform(input, output, environment);
      System.out.println("Gleux result: " + result);
   }

   /**
    * Demo parsing the gleux grammar.
    * @throws IOException if it fails
    * @throws GleuxException if it fails
    */
   @Test
   public void demoGleux() throws IOException, GleuxException {
      final Gleux parser = newGleux(readAll(reader("gleux.parse.gleux", "UTF-8")));
      final Gleux gleux = (Gleux)parser.transform(stream(reader("gleux.parse.gleux", "UTF-8")), STDOUT, singletonMap(
            "postage", new ReflectiveFunctionProvider()));
      assertNotNull(gleux.get("EndOfFile"));
   }

   /**
    * Demo source code for the gleux grammar.
    * @throws IOException if it fails
    * @throws GleuxException if it fails
    */
   @Test
   public void demoGleuxCode() throws IOException, GleuxException {
      new File("target/generated/org/fuwjin/test/generated").mkdirs();
      final Gleux gleux = newGleux(readAll(reader("gleux.parse.gleux", "UTF-8")));
      final Gleux serial = (Gleux)gleux.transform(stream(reader("gleux.code.gleux", "UTF-8")), STDOUT, singletonMap(
            "postage", new ReflectiveFunctionProvider()));
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("gleux", gleux);
      environment.put("package", "org.fuwjin.test.generated");
      environment.put("className", "Gleux");
      final Writer writer = writer("target/generated/org/fuwjin/test/generated/GleuxInterpreter.java", "UTF-8");
      try {
         serial.transform(InStream.NONE, OutStream.stream(writer), environment);
      } finally {
         writer.close();
      }
   }

   /**
    * Demo serializing the gleux grammar.
    * @throws IOException if it fails
    * @throws GleuxException if it fails
    */
   @Test
   public void demoGleuxSerial() throws IOException, GleuxException {
      new File("target/generated").mkdirs();
      final Gleux gleux = newGleux(readAll(reader("gleux.parse.gleux", "UTF-8")));
      final Gleux serial = (Gleux)gleux.transform(stream(reader("gleux.serial.gleux", "UTF-8")), STDOUT, singletonMap(
            "postage", new ReflectiveFunctionProvider()));
      final Writer writer = writer("target/generated/gleux.parse.test.gleux", "UTF-8");
      try {
         serial.transform(InStream.NONE, OutStream.stream(writer), singletonMap("gleux", gleux));
      } finally {
         writer.close();
      }
   }
}
