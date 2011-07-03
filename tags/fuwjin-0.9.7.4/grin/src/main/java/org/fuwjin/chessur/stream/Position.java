package org.fuwjin.chessur.stream;

/**
 * An I/O stream position.
 */
public interface Position {
   /**
    * Returns the current column consumed/appended.
    * @return the column
    */
   int column();

   /**
    * Returns the current index consumed/appended, of little external use.
    * @return the index
    */
   int index();

   /**
    * Returns true if the position is not an SOF/EOF, folse otherwise.
    * @return false if SOF/EOF, true otherwise
    */
   boolean isValid();

   /**
    * Returns the current line consumed/appended.
    * @return the line
    */
   int line();

   /**
    * Returns the raw value at this position.
    * @return the value
    */
   Object value();

   /**
    * Returns the string formatted value of this position.
    * @return the string value
    */
   String valueString();
}
