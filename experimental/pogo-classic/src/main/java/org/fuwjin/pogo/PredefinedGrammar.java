package org.fuwjin.pogo;

import static org.fuwjin.pogo.PogoUtils.readGrammar;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * The set of predefined grammars from the pogo package.
 */
public enum PredefinedGrammar {
   /**
    * The default pogo parser.
    */
   PogoParse("pogoParse.pogo"), //$NON-NLS-1$
   /**
    * The default pogo serializer.
    */
   PogoSerial("pogoSerial.pogo"), //$NON-NLS-1$
   /**
    * the default pogo code serializer.
    */
   PogoCodeSerial("pogoCodeSerial.pogo"); //$NON-NLS-1$
   private static final String SRC_MAIN_RESOURCES = "src/main/resources/"; //$NON-NLS-1$
   private final String file;
   private Grammar grammar;

   private PredefinedGrammar(final String file) {
      this.file = file;
   }

   /**
    * Returns the default root parser.
    * @return the root parser
    */
   public Pogo get() {
      return grammar();
   }

   /**
    * Returns the named root parser.
    * @param name the name of the root parser
    * @return the root parser
    */
   public Pogo get(final String name) {
      return grammar().get(name);
   }

   /**
    * Returns the grammar
    * @return the grammar
    */
   public Grammar grammar() {
      if(grammar == null) {
         try {
            try {
               grammar = readGrammar(new FileReader(file));
            } catch(final FileNotFoundException e) {
               grammar = readGrammar(new FileReader(SRC_MAIN_RESOURCES + file));
            }
         } catch(final Exception e) {
            throw new RuntimeException(e);
         }
         grammar.resolve();
      }
      return grammar;
   }
}
