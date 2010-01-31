package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Map;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Pogo;
import org.fuwjin.pogo.PredefinedGrammar;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * Matches any character from the input.
 */
public class CharacterParser implements Parser {
   private static final String DOT = "DOT"; //$NON-NLS-1$
   private static Pogo serial;

   @Override
   public boolean equals(final Object obj) {
      return obj instanceof CharacterParser;
   }

   @Override
   public int hashCode() {
      return hash(getClass());
   }

   @Override
   public void parse(final PogoContext context) {
      context.accept();
   }

   @Override
   public void resolve(final Map<String, Parser> grammar, final ReflectionType ruleType) {
      // do nothing
   }

   @Override
   public String toString() {
      if(serial == null) {
         serial = PredefinedGrammar.PogoSerial.grammar().get(DOT);
      }
      return serial.serial(this);
   }
}
