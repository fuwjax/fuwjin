package org.fuwjin.gleux;

import static java.util.Collections.unmodifiableCollection;
import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StringUtils.readAll;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fuwjin.gleux.GleuxInterpreter.GleuxException;
import org.fuwjin.postage.Postage;
import org.fuwjin.postage.category.ReflectionCategory;

/**
 * Manages a collection of specificatons.
 */
public class Gleux extends Transformer {
   /**
    * Creates a new instance.
    * @param input the input specification list
    * @return the new collection
    * @throws GleuxException if it fails
    */
   public static Gleux newGleux(final CharSequence input) throws GleuxException {
      return newGleux(input, new Postage(new ReflectionCategory()));
   }

   /**
    * Creates a new instance.
    * @param input the input specification list
    * @param postage the postage instance for resolving invocations
    * @return the new collection
    * @throws GleuxException if it fails
    */
   public static Gleux newGleux(final CharSequence input, final Postage postage) throws GleuxException {
      return GleuxInterpreter.interpret(input, postage);
   }

   private final Map<String, String> aliases = new LinkedHashMap<String, String>();
   private final Map<String, Specification> specs = new LinkedHashMap<String, Specification>();
   private final Map<String, Gleux> modules = new LinkedHashMap<String, Gleux>();
   private Declaration root;

   /**
    * Adds a new declaration.
    * @param decl the new declaration
    */
   public void add(final Declaration decl) {
      final Specification s = get(decl.name());
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
    * Encodes a qualified identifier into its aliased form.
    * @param qualified the qualified identifier
    * @return the qualified identifier
    */
   public String encode(final String qualified) {
      int index = qualified.lastIndexOf('.');
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
   public Specification get(final String name) {
      Specification s = specs.get(name);
      if(s == null) {
         s = new Specification(name);
         specs.put(name, s);
      }
      return s;
   }

   public Gleux getModule(final String name) {
      return modules.get(name);
   }

   public void load(final String path, final String name) throws FileNotFoundException, UnsupportedEncodingException,
         GleuxException, IOException {
      modules.put(name, newGleux(readAll(reader(path, "UTF-8"))));
   }

   public String rootName() {
      return root.name();
   }

   /**
    * Returns the set of specifications.
    * @return the specifications
    */
   public Iterable<Specification> specifications() {
      return unmodifiableCollection(specs.values());
   }

   @Override
   public State transform(final State state) {
      return root.transform(state);
   }
}
