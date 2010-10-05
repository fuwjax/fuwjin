package org.fuwjin.postage.category;

import java.util.HashMap;
import java.util.Map;

import org.fuwjin.postage.Function;
import org.fuwjin.postage.FunctionFactory;

/**
 * Manages functions for a constant pool. Constants are added to the pool with
 * the put(name, value) method. Functions are of two types, any function on
 * java.lang.Object is exposed, targeted to the value. Any other name will
 * return the value directly. Consider the following code:
 * 
 * <pre>
 * ConstantCategory c = new ConstantCategory();
 * c.put(&quot;test&quot;, &quot;test&quot;);
 * Function objectFunc = c.getFunction(&quot;test.equals&quot;);
 * objectFunc.invoke(&quot;test&quot;); // returns true
 * Function arbitraryFunc = c.getFunction(&quot;test.random&quot;);
 * arbitraryFunc.invoke(); // returns &quot;test&quot;
 * </pre>
 */
public class ConstantCategory implements FunctionFactory {
   private final Map<String, FunctionFactory> constants = new HashMap<String, FunctionFactory>();

   /**
    * Creates a new instance. By default this instance has a "null" object.
    */
   public ConstantCategory() {
      constants.put("null", new NullCategory());
   }

   /**
    * Creates a new instance. This instance does not contain a "null" object.
    * @param name the name of the initial constant
    * @param value the value of the initial constant
    */
   public ConstantCategory(final String name, final Object value) {
      put(name, value);
   }

   @Override
   public Function getFunction(final String name) {
      final int index = name.lastIndexOf('.');
      if(index < 0) {
         return null;
      }
      final String constant = name.substring(0, index);
      final FunctionFactory instance = constants.get(constant);
      if(instance == null) {
         return null;
      }
      final String method = name.substring(index + 1);
      final Function function = instance.getFunction(method);
      if(function != null) {
         return function;
      }
      return instance.getFunction("this");
   }

   /**
    * Adds a value to the constant pool. The value is added to the pool by the
    * name. Any previous value by the same name will be discarded with extreme
    * prejudice.
    * @param name the name of the constant
    * @param value the value of the constant
    * @return this
    */
   public ConstantCategory put(final String name, final Object value) {
      return put(name, value, Object.class);
   }

   /**
    * Adds a value to the constant pool. The value is added to the pool by the
    * name and type. Any previous value by the same name will be discarded with
    * extreme prejudice.
    * @param <T> the type of the constant
    * @param name the name of the constant
    * @param value the value of the constant
    * @param type the type of the constant
    * @return this
    */
   public <T> ConstantCategory put(final String name, final T value, final Class<T> type) {
      constants.put(name, new InstanceCategory(value, type));
      return this;
   }
}
