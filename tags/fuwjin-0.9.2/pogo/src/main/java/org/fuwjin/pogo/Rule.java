package org.fuwjin.pogo;

import org.fuwjin.postage.Category;

/**
 * A rule is a named parser.
 */
public interface Rule extends Parser {
   /**
    * Returns the category.
    * @return the category
    */
   Category category();

   /**
    * Returns the name.
    * @return the name
    */
   String name();
}
