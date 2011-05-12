package org.fuwjin.chessur.expression;

import org.fuwjin.chessur.stream.Environment;
import org.fuwjin.chessur.stream.SinkStream;
import org.fuwjin.chessur.stream.SourceStream;
import org.fuwjin.dinah.Function;

public class FieldTemplate implements Expression {
   private final Function setter;
   private final Expression value;
   private final String name;

   public FieldTemplate(final String name, final Function setter, final Expression value) {
      this.name = name;
      this.setter = setter;
      this.value = value;
   }

   public Object invoke(final Object object, final Object val) throws Exception {
      return setter.invoke(object, val);
   }

   @Override
   public Object resolve(final SourceStream input, final SinkStream output, final Environment scope)
         throws AbortedException, ResolveException {
      return value.resolve(input, output, scope);
   }

   String name() {
      return name;
   }
}
