package org.fuwjin.pogo;


/**
 * A rule is a named parser.
 */
public interface Rule extends Parser {
   /**
    * Returns the category.
    * @return the category
    */
   String category();

   /**
    * Returns the name.
    * @return the name
    */
   String name();
}
