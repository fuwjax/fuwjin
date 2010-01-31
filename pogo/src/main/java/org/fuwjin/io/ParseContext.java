package org.fuwjin.io;

import static java.lang.Character.isHighSurrogate;
import static java.lang.Character.toCodePoint;

/**
 * The standard PogoContext for parsing. The CharSequence input should implement
 * the contract as specified by the CharSequence interface. However not all of
 * the contract constraints are necessary. In particular, this class expects the
 * following relaxed contract from a CharSequence implementation:
 * <p>
 * If the last successful call to charAt took an index=x, then
 * <ul>
 * <li>any subsequent call to charAt(x) should return the same character</li>
 * <li>the next call to charAt will take an index <= x+1</li>
 * <li>any subsequent call to subSequence(s,x+1) should be identical to
 * subSequence(s,x) concatenated with charAt(x) for any s <= x</li>
 * <li>a call to subSequence will take start <= end <= x+1</li>
 * </ul>
 * If the next call to charAt was unsuccessful at index=x+1, i.e. resulted in an
 * IndexOutOfBoundsException thrown, then any call to charAt(y) where y>=x
 * should result in an IndexOutOfBoundsException thrown. Note that in particular
 * the length and toString methods are not used here and therefore may be
 * adjusted to need. In particular, wrapping a suitably buffered InputStream
 * with a CharSequence is acceptable.
 */
public class ParseContext extends RootContext {
   private static final String UNEXPECTED_CHAR = "UC-"; //$NON-NLS-1$
   private static final String ERROR_CODE = "UR-"; //$NON-NLS-1$
   private static final String UNEXPECTED_END_OF_INPUT = "Unexpected end of input"; //$NON-NLS-1$
   private int position;
   private final CharSequence input;

   /**
    * Creates a new instance.
    * @param input the input stream
    * @param object the initial value of the result object, may be null
    */
   public ParseContext(final CharSequence input, final Object object) {
      super(object);
      this.input = input;
   }

   @Override
   public void accept() {
      read();
   }

   @Override
   public void accept(final int ch) {
      final int mark = position();
      if(!success(read() == ch, UNEXPECTED_CHAR + (char)ch)) {
         seek(mark);
      }
   }

   @Override
   public void accept(final int start, final int end) {
      final int mark = position();
      final int current = read();
      if(!success(current >= start && current <= end, ERROR_CODE + (char)start + (char)end)) {
         seek(mark);
      }
   }

   @Override
   public boolean hasRemaining() {
      try {
         input.charAt(position);
         return true;
      } catch(final IndexOutOfBoundsException e) {
         return false;
      }
   }

   @Override
   public PogoContext newChild() {
      return new ChildContext(this);
   }

   @Override
   public int position() {
      return position;
   }

   @Override
   public void seek(final int mark) {
      position = mark;
   }

   @Override
   public String substring(final int mark) {
      return input.subSequence(mark, position).toString();
   }

   private int read() {
      try {
         final char c1 = input.charAt(position);
         ++position;
         if(isHighSurrogate(c1)) {
            final char c2 = input.charAt(position);
            ++position;
            success(true, null);
            return toCodePoint(c1, c2);
         }
         success(true, null);
         return c1;
      } catch(final IndexOutOfBoundsException e) {
         success(false, UNEXPECTED_END_OF_INPUT);
         return 0;
      }
   }
}
