package org.fuwjin.pogo.parser;

/**
 * Captures the precedence levels for the various parsers.
 */
public enum PrintLevel {
   /**
    * The character class level.
    */
   LITERAL_OPTION {
      @Override
      protected String escape(final char ch) {
         return getCh(ch, "\n\r\t\\[]", "nrt\\[]"); //$NON-NLS-1$ //$NON-NLS-2$
      }
   },
   /**
    * The string literal level.
    */
   LITERAL_SEQUENCE {
      @Override
      protected String escape(final char ch) {
         return getCh(ch, "\n\r\t\\'", "nrt\\'"); //$NON-NLS-1$ //$NON-NLS-2$
      }
   };
   /**
    * Encodes a character for the current level.
    * @param ch the character to encode
    * @return the encoded character
    */
   protected abstract String escape(final char ch);

   /**
    * Encodes the character for the given dirty and escaped character pairs.
    * @param ch the character to encode
    * @param dirty the set of encodable characters
    * @param escaped the mappend encoded characters
    * @return the encoded character
    */
   String getCh(final char ch, final String dirty, final String escaped) {
      final int index = dirty.indexOf(ch);
      if(index >= 0) {
         return new String(new char[]{'\\', escaped.charAt(index)});
      }
      return Character.toString(ch);
   }
}
