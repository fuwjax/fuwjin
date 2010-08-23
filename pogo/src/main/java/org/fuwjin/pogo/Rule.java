package org.fuwjin.pogo;

import org.fuwjin.postage.Function;

public interface Rule extends Parser {
   Function getFunction(String name);

   String name();
}
