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
package org.fuwjin.chessur.expression;

import static java.util.Collections.unmodifiableCollection;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManager;
import org.fuwjin.chessur.Script;
import org.fuwjin.chessur.generated.ChessurInterpreter.ChessurException;
import org.fuwjin.dinah.FunctionSignature;

/**
 * Manages a collection of specificatons.
 */
public class CatalogImpl extends Executable implements Catalog {
   private final Map<String, String> aliases = new LinkedHashMap<String, String>();
   private final Map<String, FunctionSignature> signatures = new LinkedHashMap<String, FunctionSignature>();
   private final Map<String, ScriptImpl> scripts = new LinkedHashMap<String, ScriptImpl>();
   private final Map<String, Catalog> modules = new LinkedHashMap<String, Catalog>();
   private Declaration root;
   private final CatalogManager manager;

   /**
    * Creates a new instance.
    * @param name the name of the catalog
    * @param manager the manager for loading referenced catalogs
    */
   public CatalogImpl(final String name, final CatalogManager manager) {
      this.manager = manager;
   }

   /**
    * Adds a new declaration.
    * @param decl the new declaration
    */
   public void add(final Declaration decl) {
      final ScriptImpl s = (ScriptImpl)get(decl.name());
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

   /**
    * Creates an alias for a signature.
    * @param signature the signature to alias
    * @param name the alias
    */
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
   @Override
   public Script get(final String name) {
      ScriptImpl s = scripts.get(name);
      if(s == null) {
         s = new ScriptImpl(name);
         scripts.put(name, s);
      }
      return s;
   }

   /**
    * Returns a referenced catalog.
    * @param name the name of the catalog
    * @return the catalog
    */
   public Catalog getModule(final String name) {
      return modules.get(name);
   }

   /**
    * Returns a referenced signature.
    * @param name the name of the signature
    * @return the signature
    */
   public FunctionSignature getSignature(final String name) {
      final FunctionSignature signature = signatures.get(name);
      if(signature == null) {
         return new FunctionSignature(name, -1);
      }
      return signature;
   }

   /**
    * Loads a catalog.
    * @param path the path to the catalog
    * @param name the name of the catalog
    * @throws ChessurException if the catalog cannot be loaded
    * @throws IOException if the path does not refer to a file
    */
   public void load(final String path, final String name) throws ChessurException, IOException {
      modules.put(name, manager.loadCat(path));
   }

   @Override
   public String name() {
      return root.name();
   }

   /**
    * Returns the name of the primary script.
    * @return the primary script name
    */
   public String rootName() {
      return root.name();
   }

   /**
    * Returns the set of scripts.
    * @return the scripts
    */
   @Override
   public Iterable<? extends Script> scripts() {
      return unmodifiableCollection(scripts.values());
   }

   @Override
   protected Expression expression() {
      return root;
   }
}
