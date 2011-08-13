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

import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StreamUtils.writer;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.ChessurScript;
import org.fuwjin.grin.env.StandardTrace;
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

   private ScriptEngine engine;
   private Bindings env;

   /**
    * Demo parsing the grin grammar.
    * @throws Exception if it fails
    */
   @Test
   public void demoGrin() throws Exception {
      final CompiledScript parser = ((Compilable)engine).compile(reader("org/fuwjin/chessur/generated/GrinParser.cat",
            "UTF-8"));
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(reader("org/fuwjin/chessur/generated/GrinParser.cat", "UTF-8"));
      context.setWriter(new OutputStreamWriter(System.out));
      context.setBindings(env, ScriptContext.ENGINE_SCOPE);
      final Catalog grin = (Catalog)parser.eval(context);
      assertNotNull(grin.get("EndOfFile"));
   }

   /**
    * Demo source code for the grin grammar.
    * @throws Exception if it fails
    */
   @Test
   public void demoGrinCode() throws Exception {
      new File("target/generated/org/fuwjin/chessur/generated").mkdirs();
      final CompiledScript cat = ((Compilable)engine).compile(reader("org/fuwjin/chessur/generated/GrinParser.cat",
            "UTF-8"));
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(reader("org/fuwjin/chessur/generated/GrinCodeGenerator.cat", "UTF-8"));
      context.setWriter(new OutputStreamWriter(System.out));
      context.setBindings(env, ScriptContext.ENGINE_SCOPE);
      final Catalog serial = (Catalog)cat.eval(context);
      final Bindings environment = new SimpleBindings();
      environment.put("cat", ((ChessurScript)cat).catalog());
      environment.put("package", "org.fuwjin.chessur.generated");
      environment.put("className", "Chessur");
      final Writer writer = writer("target/generated/org/fuwjin/chessur/generated/Chessur.java", "UTF-8");
      try {
         serial.eval(new StandardTrace(null, writer, environment, null));
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
      final CompiledScript serial = ((Compilable)engine).compile(reader(
            "org/fuwjin/chessur/generated/GrinCodeGenerator.cat", "UTF-8"));
      final Bindings environment = engine.createBindings();
      environment.put("cat", ((ChessurScript)serial).catalog());
      environment.put("package", "org.fuwjin.chessur.generated");
      environment.put("className", "ChessurCodeGen");
      final Writer writer = writer("target/generated/org/fuwjin/chessur/generated/ChessurCodeGen.java", "UTF-8");
      final ScriptContext context = new SimpleScriptContext();
      context.setWriter(writer);
      context.setBindings(environment, ScriptContext.ENGINE_SCOPE);
      try {
         serial.eval(context);
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
      final CompiledScript cat = ((Compilable)engine).compile(reader("org/fuwjin/chessur/generated/GrinParser.cat",
            "UTF-8"));
      final ScriptContext context = new SimpleScriptContext();
      context.setReader(reader("org/fuwjin/chessur/generated/GrinSerializer.cat", "UTF-8"));
      context.setWriter(new OutputStreamWriter(System.out));
      context.setBindings(env, ScriptContext.ENGINE_SCOPE);
      final Catalog serial = (Catalog)cat.eval(context);
      final Writer writer = writer("target/generated/grin.parse.test.cat", "UTF-8");
      final Bindings bindings = new SimpleBindings();
      bindings.put("cat", ((ChessurScript)cat).catalog());
      try {
         serial.eval(new StandardTrace(null, writer, bindings, null));
      } finally {
         writer.close();
      }
   }

   /**
    * Sets up the test.
    */
   @Before
   public void setup() {
      final ScriptEngineManager engines = new ScriptEngineManager();
      engine = engines.getEngineByName("chessur");
      env = engine.createBindings();
      env.put("name", "test");
   }
}
