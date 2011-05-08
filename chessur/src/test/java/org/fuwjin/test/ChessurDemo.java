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
import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StreamUtils.writer;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.fuwjin.chessur.CatalogManager;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.generated.ChessurInterpreter.ChessurException;
import org.fuwjin.dinah.ReflectiveFunctionProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * Demos parsing a file.
 */
public class ChessurDemo {
   public static void echo(final String message) {
      System.out.println("echoing " + message);
   }

   private CatalogManager manager;
   private Map<String, Object> env;

   @Test
   public void demoForSatish() throws Exception {
      final Catalog parser = manager.loadCat("grin.parse.cat");
      final Catalog grin = (Catalog)parser.exec(reader("satish.cat", "UTF-8"), System.out, env);
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("list", Arrays.asList(1, 2, 3));
      final Object result = grin.exec(System.in, System.out, environment);
      System.out.println("Grin result: " + result);
   }

   /**
    * Demo parsing the grin grammar.
    * @throws IOException if it fails
    * @throws ChessurException if it fails
    */
   @Test
   public void demoGrin() throws Exception {
      final Catalog parser = manager.loadCat("grin.parse.cat");
      final Catalog grin = (Catalog)parser.exec(reader("grin.parse.cat", "UTF-8"), System.out, env);
      assertNotNull(grin.get("EndOfFile"));
   }

   /**
    * Demo source code for the grin grammar.
    * @throws IOException if it fails
    * @throws ChessurException if it fails
    */
   @Test
   public void demoGrinCode() throws Exception {
      new File("target/generated/org/fuwjin/chessur/generated").mkdirs();
      final Catalog cat = manager.loadCat("grin.parse.cat");
      final Catalog serial = (Catalog)cat.exec(reader("grin.code.cat", "UTF-8"), System.out, env);
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("cat", cat);
      environment.put("package", "org.fuwjin.chessur.generated");
      environment.put("className", "Chessur");
      final Writer writer = writer("target/generated/org/fuwjin/chessur/generated/ChessurInterpreter.java", "UTF-8");
      try {
         serial.exec(writer, environment);
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
   public void demoGrinSerial() throws Exception {
      new File("target/generated").mkdirs();
      final Catalog cat = manager.loadCat("grin.parse.cat");
      final Catalog serial = (Catalog)cat.exec(reader("grin.serial.cat", "UTF-8"), System.out, env);
      final Writer writer = writer("target/generated/grin.parse.test.cat", "UTF-8");
      try {
         serial.exec(writer, singletonMap("cat", cat));
      } finally {
         writer.close();
      }
   }

   @Before
   public void setup() {
      manager = new CatalogManager();
      env = new HashMap<String, Object>();
      env.put("postage", new ReflectiveFunctionProvider());
      env.put("name", "test");
      env.put("manager", manager);
   }
}
