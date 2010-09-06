package org.fuwjin.postage;

import org.fuwjin.postage.function.CompositeFunction;

public interface Category {
   CompositeFunction getFunction(String name);

   String name();
}
