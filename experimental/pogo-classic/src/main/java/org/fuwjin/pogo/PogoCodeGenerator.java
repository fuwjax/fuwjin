package org.fuwjin.pogo;

import static org.fuwjin.pogo.PredefinedGrammar.PogoCodeSerial;

/**
 * The wrapper object for generating the code from a pogo grammar.
 */
public class PogoCodeGenerator {
   private final String qualifiedPackage;
   private final String name;
   private final Grammar grammar;

   /**
    * Creates a new instance.
    * @param qualifiedName the qualified class name of the generated source
    * @param grammar the grammar to encode
    */
   public PogoCodeGenerator(final String qualifiedName, final Grammar grammar) {
      this.grammar = grammar;
      final int index = qualifiedName.lastIndexOf('.');
      qualifiedPackage = qualifiedName.substring(0, index);
      name = qualifiedName.substring(index + 1);
   }

   /**
    * Returns the source code.
    * @return the source code
    */
   public String toCode() {
      return PogoCodeSerial.get().serial(this);
   }

   @Override
   public String toString() {
      return qualifiedPackage + '.' + name + '=' + grammar;
   }
}
