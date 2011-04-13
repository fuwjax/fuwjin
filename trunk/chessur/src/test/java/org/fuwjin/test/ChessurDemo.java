/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import static java.util.Collections.singletonMap;
import static org.fuwjin.chessur.Grin.newGrin;
import static org.fuwjin.chessur.InStream.STDIN;
import static org.fuwjin.chessur.InStream.stream;
import static org.fuwjin.chessur.OutStream.STDOUT;
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

import org.fuwjin.chessur.ChessurInterpreter.ChessurException;
import org.fuwjin.chessur.Grin;
import org.fuwjin.chessur.InStream;
import org.fuwjin.chessur.OutStream;
import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.junit.Test;

/**
 * Demos parsing a file.
 */
public class ChessurDemo {
   public static void echo(final String message) {
      System.out.println("echoing " + message);
   }

   @Test
   public void demoForSatish() throws IOException, ChessurException {
      final Grin parser = newGrin(readAll(reader("grin.parse.cat", "UTF-8")));
      final Grin grin = (Grin)parser.transform(InStream.stream(reader("satish.cat", "UTF-8")), OutStream.STDOUT,
            Collections.<String, Object> singletonMap("postage", new ReflectiveFunctionProvider()));
      final InStream input = STDIN;
      final OutStream output = STDOUT;
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("list", Arrays.asList(1, 2, 3));
      final Object result = grin.transform(input, output, environment);
      System.out.println("Grin result: " + result);
   }

   /**
    * Demo parsing the grin grammar.
    * @throws IOException if it fails
    * @throws ChessurException if it fails
    */
   @Test
   public void demoGrin() throws IOException, ChessurException {
      final Grin parser = newGrin(readAll(reader("grin.parse.cat", "UTF-8")));
      final Grin grin = (Grin)parser.transform(stream(reader("grin.parse.cat", "UTF-8")), STDOUT, singletonMap(
            "postage", new ReflectiveFunctionProvider()));
      assertNotNull(grin.get("EndOfFile"));
   }

   /**
    * Demo source code for the grin grammar.
    * @throws IOException if it fails
    * @throws ChessurException if it fails
    */
   @Test
   public void demoGrinCode() throws IOException, ChessurException {
      new File("target/generated/org/fuwjin/test/generated").mkdirs();
      final Grin grin = newGrin(readAll(reader("grin.parse.cat", "UTF-8")));
      final Grin serial = (Grin)grin.transform(stream(reader("grin.code.cat", "UTF-8")), STDOUT, singletonMap(
            "postage", new ReflectiveFunctionProvider()));
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("grin", grin);
      environment.put("package", "org.fuwjin.test.generated");
      environment.put("className", "Chessur");
      final Writer writer = writer("target/generated/org/fuwjin/test/generated/ChessurInterpreter.java", "UTF-8");
      try {
         serial.transform(InStream.NONE, OutStream.stream(writer), environment);
      } finally {
         writer.close();
      }
   }

   /**
    * Demo serializing the grin grammar.
    * @throws IOException if it fails
    * @throws ChessurException if it fails
    */
   @Test
   public void demoGrinSerial() throws IOException, ChessurException {
      new File("target/generated").mkdirs();
      final Grin grin = newGrin(readAll(reader("grin.parse.cat", "UTF-8")));
      final Grin serial = (Grin)grin.transform(stream(reader("grin.serial.cat", "UTF-8")), STDOUT, singletonMap(
            "postage", new ReflectiveFunctionProvider()));
      final Writer writer = writer("target/generated/grin.parse.test.cat", "UTF-8");
      try {
         serial.transform(InStream.NONE, OutStream.stream(writer), singletonMap("grin", grin));
      } finally {
         writer.close();
      }
   }
}
