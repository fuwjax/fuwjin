package org.fuwjin.pogo.postage;

import java.util.Iterator;

import org.fuwjin.postage.Postage;
import org.fuwjin.postage.StandardAdaptable;
import org.fuwjin.postage.category.VoidCategory;
import org.fuwjin.postage.function.AbstractFunction;
import org.fuwjin.postage.function.ConstantFunction;

public class PogoPostage extends Postage {
   public PogoPostage() {
      super(new ConstantFunction("default", true), new ReturnFunction(), new ConstantFunction("true",
            StandardAdaptable.TRUE), new ConstantFunction("null", StandardAdaptable.NULL), new ConstantFunction(
            "false", StandardAdaptable.FALSE), new AbstractFunction("this", Object.class, true, Object.class,
            Object[].class) {
         @Override
         public Object tryInvoke(final Object... args) {
            return args[0];
         }
      }, new AbstractFunction("next", Object.class, false, Iterator.class) {
         @Override
         public Object tryInvoke(final Object... args) {
            return ((Iterator<?>)args[0]).next();
         }
      });
      addCategory(new VoidCategory("default", this));
   }
}
