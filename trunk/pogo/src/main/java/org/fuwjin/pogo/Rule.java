package org.fuwjin.pogo;

import org.fuwjin.postage.Category;

public interface Rule extends Parser {
   Category category();

   String name();
}
