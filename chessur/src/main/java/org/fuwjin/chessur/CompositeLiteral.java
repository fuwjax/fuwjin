package org.fuwjin.chessur;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a composite or dynamic literal.
 */
public class CompositeLiteral implements Expression {
   /**
    * Translates the code point to an escaped character if it's necessary
    * @param ch the code point
    * @return the escaped character
    */
   public static String escape(final int ch) {
      switch(ch) {
         case '\'':
            return "\\'";
         case '"':
            return "\\\"";
         case '<':
            return "\\<";
      }
      return Literal.standardEscape(ch);
   }

   private final List<Expression> values = new ArrayList<Expression>();

   /**
    * Append a value.
    * @param value the value
    */
   public void append(final Expression value) {
      values.add(value);
   }

   /**
    * Append a code point.
    * @param codepoint the code point
    */
   public void appendChar(final int codepoint) {
      Literal lit;
      final Expression last = last();
      if(last instanceof Literal) {
         lit = (Literal)last;
      } else {
         lit = new Literal();
         values.add(lit);
      }
      lit.append(codepoint);
   }

   /**
    * Append a code point.
    * @param codepoint the code point as a string
    */
   public void appendString(final String codepoint) {
      appendChar(codepoint.codePointAt(0));
   }

   protected Expression last() {
      return values.size() == 0 ? null : values.get(values.size() - 1);
   }

   @Override
   public String toString() {
      final StringBuilder builder = new StringBuilder("\"");
      for(final Expression value: values) {
         builder.append(value);
      }
      return builder.append("\"").toString();
   }

   @Override
   public State transform(final State state) {
      final StringBuilder builder = new StringBuilder();
      State result = state;
      for(final Expression value: values) {
         result = value.transform(result);
         if(!result.isSuccess()) {
            return result;
         }
         builder.append(result.value());
      }
      return result.value(builder.toString());
   }
}
