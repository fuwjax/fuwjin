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
package org.fuwjin.chessur;

import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableCollection;
import static org.fuwjin.chessur.ChessurInterpreter.interpret;
import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StringUtils.readAll;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.fuwjin.chessur.ChessurInterpreter.ChessurException;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.FunctionSignature;
import org.fuwjin.dinah.ReflectiveFunctionProvider;

/**
 * Manages a collection of specificatons.
 */
public class Catalog extends Transformer {
   /**
    * Creates a new instance.
    * @param input the input specification list
    * @return the new collection
    * @throws ChessurException if it fails
    */
   public static Catalog loadCat(final CharSequence input) throws ChessurException {
      return loadCat(input, new ReflectiveFunctionProvider());
   }

   /**
    * Creates a new instance.
    * @param input the input specification list
    * @param postage the postage instance for resolving invocations
    * @return the new collection
    * @throws ChessurException if it fails
    */
   public static Catalog loadCat(final CharSequence input, final FunctionProvider postage) throws ChessurException {
      return (Catalog)interpret(input, new StringBuilder(), singletonMap("postage", postage));
   }

   private final Map<String, String> aliases = new LinkedHashMap<String, String>();
   private final Map<String, FunctionSignature> signatures = new LinkedHashMap<String, FunctionSignature>();
   private final Map<String, Script> scripts = new LinkedHashMap<String, Script>();
   private final Map<String, Catalog> modules = new LinkedHashMap<String, Catalog>();
   private Declaration root;

   /**
    * Adds a new declaration.
    * @param decl the new declaration
    */
   public void add(final Declaration decl) {
      final Script s = get(decl.name());
      s.setDecl(decl);
      if(root == null) {
         root = decl;
      }
   }

   /**
    * Returns an aliased qualified name.
    * @param name the alias
    * @return the qualified name, or name if none exists
    */
   public String alias(final String name) {
      final String alias = aliases.get(name);
      if(alias == null) {
         return name;
      }
      return alias;
   }

   /**
    * Creates a new alias.
    * @param qname the qualified name to alias
    * @param name the alias
    */
   public void alias(final String qname, final String name) {
      aliases.put(name, qname);
   }

   /**
    * Returns the set of aliases.
    * @return the aliases
    */
   public Iterable<Map.Entry<String, String>> aliases() {
      return unmodifiableCollection(aliases.entrySet());
   }

   public void aliasSignature(final FunctionSignature signature, final String name) {
      signatures.put(name, signature);
   }

   /**
    * Encodes a qualified identifier into its aliased form.
    * @param qualified the qualified identifier
    * @return the qualified identifier
    */
   public String encode(final String qualified) {
      int index = qualified.length();
      while(index > -1) {
         final String prefix = qualified.substring(0, index);
         for(final Map.Entry<String, String> entry: aliases.entrySet()) {
            if(entry.getValue().equals(prefix)) {
               return entry.getKey() + qualified.substring(index);
            }
         }
         index = qualified.lastIndexOf('.', index - 1);
      }
      return qualified;
   }

   /**
    * Returns a specification.
    * @param name the name of the specification
    * @return the specification
    */
   public Script get(final String name) {
      Script s = scripts.get(name);
      if(s == null) {
         s = new Script(name);
         scripts.put(name, s);
      }
      return s;
   }

   public Catalog getModule(final String name) {
      return modules.get(name);
   }

   public FunctionSignature getSignature(final String name) {
      final FunctionSignature signature = signatures.get(name);
      if(signature == null) {
         return new FunctionSignature(name, -1);
      }
      return signature;
   }

   public void load(final String path, final String name) throws FileNotFoundException, UnsupportedEncodingException,
         ChessurException, IOException {
      modules.put(name, loadCat(readAll(reader(path, "UTF-8"))));
   }

   public String rootName() {
      return root.name();
   }

   /**
    * Returns the set of scripts.
    * @return the scripts
    */
   public Iterable<Script> scripts() {
      return unmodifiableCollection(scripts.values());
   }

   @Override
   public State transform(final State state) {
      return root.transform(state);
   }
}
