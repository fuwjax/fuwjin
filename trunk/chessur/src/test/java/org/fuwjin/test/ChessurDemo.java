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
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManagerImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Demos parsing a file.
 */
public class ChessurDemo {
   /**
    * Sample method for Satish's demo.
    * @param message the argument
    */
   public static void echo(final String message) {
      System.out.println("echoing " + message);
   }

   private CatalogManagerImpl manager;
   private Map<String, Object> env;

   /**
    * Demo parsing the grin grammar.
    * @throws Exception if it fails
    */
   @Test
   public void demoGrin() throws Exception {
      final Catalog parser = manager.loadCat("org/fuwjin/chessur/generated/GrinParser.cat");
      final Catalog grin = (Catalog)parser.exec(reader("org/fuwjin/chessur/generated/GrinParser.cat", "UTF-8"),
            System.out, env);
      assertNotNull(grin.get("EndOfFile"));
   }

   /**
    * Demo source code for the grin grammar.
    * @throws Exception if it fails
    */
   @Test
   public void demoGrinCode() throws Exception {
      new File("target/generated/org/fuwjin/chessur/generated").mkdirs();
      final Catalog cat = manager.loadCat("org/fuwjin/chessur/generated/GrinParser.cat");
      final Catalog serial = (Catalog)cat.exec(reader("org/fuwjin/chessur/generated/GrinCodeGenerator.cat", "UTF-8"),
            System.out, env);
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("cat", cat);
      environment.put("package", "org.fuwjin.chessur.generated");
      environment.put("className", "Chessur");
      final Writer writer = writer("target/generated/org/fuwjin/chessur/generated/Chessur.java", "UTF-8");
      try {
         serial.exec(writer, environment);
      } finally {
         writer.close();
      }
   }

   /**
    * Demo source code for the grin code generator.
    * @throws Exception if it fails
    */
   @Test
   public void demoGrinCodeCode() throws Exception {
      new File("target/generated/org/fuwjin/chessur/generated").mkdirs();
      final Catalog serial = manager.loadCat("org/fuwjin/chessur/generated/GrinCodeGenerator.cat");
      final Map<String, Object> environment = new HashMap<String, Object>();
      environment.put("cat", serial);
      environment.put("package", "org.fuwjin.chessur.generated");
      environment.put("className", "ChessurCodeGen");
      final Writer writer = writer("target/generated/org/fuwjin/chessur/generated/ChessurCodeGen.java", "UTF-8");
      try {
         serial.exec(writer, environment);
      } finally {
         writer.close();
      }
   }

   /**
    * Demo serializing the grin grammar.
    * @throws Exception if it fails
    */
   @Test
   public void demoGrinSerial() throws Exception {
      new File("target/generated").mkdirs();
      final Catalog cat = manager.loadCat("org/fuwjin/chessur/generated/GrinParser.cat");
      final Catalog serial = (Catalog)cat.exec(reader("org/fuwjin/chessur/generated/GrinSerializer.cat", "UTF-8"),
            System.out, env);
      final Writer writer = writer("target/generated/grin.parse.test.cat", "UTF-8");
      try {
         serial.exec(writer, singletonMap("cat", cat));
      } finally {
         writer.close();
      }
   }

   /**
    * Sets up the test.
    */
   @Before
   public void setup() {
      manager = new CatalogManagerImpl();
      env = new HashMap<String, Object>();
      env.put("name", "test");
      env.put("manager", manager);
   }
}
