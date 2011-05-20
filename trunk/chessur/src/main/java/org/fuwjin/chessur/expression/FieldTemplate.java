package org.fuwjin.chessur.expression;

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.Function;

/**
 * An abstraction for a field value.
 */
public class FieldTemplate implements Expression {
   private final Function setter;
   private final Expression value;
   private final String name;

   /**
    * Creates a new instance.
    * @param name the name of the field
    * @param setter the function for mutating the field
    * @param value the new value for the field
    */
   public FieldTemplate(final String name, final Function setter, final Expression value) {
      this.name = name;
      this.setter = setter;
      this.value = value;
   }

   /**
    * Sets the field of the object to the new value.
    * @param object the object to mutate
    * @param val the new value
    * @return the result of the method, usually "UNSET"
    * @throws Exception if the invocation fails
    */
   public Object invoke(final Object object, final Object val) throws Exception {
      return setter.invoke(object, val);
   }

   /**
    * Returns the field name.
    * @return the name
    */
   public String name() {
      return name;
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      return value.resolve(input, output, scope);
   }

   /**
    * Returns the field mutator function.
    * @return the setter
    */
   public Function setter() {
      return setter;
   }

   /**
    * Returns the field value.
    * @return the value
    */
   public Expression value() {
      return value;
   }
}
