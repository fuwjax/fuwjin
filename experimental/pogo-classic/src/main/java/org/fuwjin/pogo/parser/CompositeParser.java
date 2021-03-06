package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Pogo;
import org.fuwjin.pogo.PredefinedGrammar;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * A base class for composite parsers.
 */
public abstract class CompositeParser implements Parser, Iterable<Parser> {
   private static final String EXPRESSION = "Expression"; //$NON-NLS-1$
   private final List<Parser> parsers = new LinkedList<Parser>();
   private static Pogo serial;

   /**
    * Adds a parser to this composite.
    * @param parser the parser to add
    */
   public void add(final Parser parser) {
      parsers.add(parser);
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final CompositeParser o = (CompositeParser)obj;
         return eq(getClass(), o.getClass()) && eq(parsers, o.parsers);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), parsers);
   }

   public Iterator<Parser> iterator() {
      return parsers.iterator();
   }

   /**
    * Attempts to reduce the structure of this composite. The return value
    * should be used in place of the parser that generated it.
    * @return the reduced parser
    */
   public Parser reduce() {
      for(int index = 0; index < parsers.size(); index++) {
         final Parser parser = parsers.get(index);
         if(parser instanceof CompositeParser) {
            parsers.set(index, ((CompositeParser)parser).reduce());
         }
      }
      if(parsers.size() == 1) {
         return parsers.get(0);
      }
      return this;
   }

   @Override
   public void resolve(final Map<String, Parser> grammar, final ReflectionType ruleType) {
      for(final Parser parser: parsers) {
         parser.resolve(grammar, ruleType);
      }
   }

   @Override
   public String toString() {
      if(serial == null) {
         serial = PredefinedGrammar.PogoSerial.grammar().get(EXPRESSION);
      }
      return serial.serial(this);
   }

   /**
    * Returns true if this composite parser is composed only of literal parsers.
    * @return true if a literal composite, false otherwise
    */
   protected boolean isLiteral() {
      for(final Parser parser: parsers) {
         if(!isLiteral(parser)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Returns true if {@code parser} is a literal parser according to this
    * composite.
    * @param parser the parser which may be literal
    * @return true if {@code parser} is literal, false otherwise
    */
   protected abstract boolean isLiteral(Parser parser);
}
