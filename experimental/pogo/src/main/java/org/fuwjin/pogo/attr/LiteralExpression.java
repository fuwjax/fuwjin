package org.fuwjin.pogo.attr;

import java.util.Map;

import org.fuwjin.pogo.CodePointStream;
import org.fuwjin.pogo.CodePointStreamFactory;
import org.fuwjin.util.CodePointList;

/**
 * Matches a literal sequence.
 */
public class LiteralExpression implements Expression {
   /**
    * The newline character.
    */
   public static final int NEWLINE = '\n';
   /**
    * The carriage return character.
    */
   public static final int RETURN = '\r';
   /**
    * The horizontal tab character.
    */
   public static final int TAB = '\t';

   /**
    * Returns the code point for the first character in the value.
    * @param value the string which needs a code point
    * @return the code point
    */
   public static int codepoint(final String value) {
      final char c1 = value.charAt(0);
      if(Character.isHighSurrogate(c1)) {
         return Character.toCodePoint(c1, value.charAt(1));
      }
      return c1;
   }

   /**
    * Returns true when the first character of value is an identifier part.
    * @param value the test value
    * @return true if the value is an identifier part, false otherwise
    */
   public static boolean isIdentifierPart(final String value) {
      return Character.isJavaIdentifierPart(codepoint(value));
   }

   /**
    * Returns true when the first character of value is an identifier start.
    * @param value the test value
    * @return true if the value is an identifier start, false otherwise
    */
   public static boolean isIdentifierStart(final String value) {
      return Character.isJavaIdentifierStart(codepoint(value));
   }

   /**
    * Parses a hex string into an integer.
    * @param value the hex string
    * @return the integer
    */
   public static int parseHex(final String value) {
      return Integer.parseInt(value, 16);
   }

   /**
    * Parses an octal string into an integer.
    * @param value the octal string
    * @return the integer
    */
   public static int parseOctal(final String value) {
      return Integer.parseInt(value, 8);
   }

   private final CodePointList match = new CodePointList();

   /**
    * Creates a new instance.
    */
   public LiteralExpression() {
      // do nothing
   }

   /**
    * Creates a new instance.
    * @param literal the expected literal
    */
   public LiteralExpression(final String literal) {
      final CodePointStream reader = CodePointStreamFactory.streamOf(literal);
      int ch = reader.next();
      while(ch != CodePointStream.EOF) {
         match.add(ch);
         ch = reader.next();
      }
   }

   /**
    * Appends a codePoint to this literal.
    * @param codePoint the codepoint to append
    */
   public void append(final int codePoint) {
      match.add(codePoint);
   }

   @Override
   public State transition(final State state, final Map<String, Object> scope) {
      State s = state;
      for(final int codePoint: match) {
         if(s.codePoint() != codePoint) {
            return state.failure("expected '%c' but received '%c'", codePoint, s.codePoint());
         }
         s = s.next();
         if(!s.isSuccess()) {
            return state.failure(s, "Unexpected failure when parsing %s", match);
         }
      }
      return s;
   }
}
