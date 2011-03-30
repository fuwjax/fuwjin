package org.fuwjin.gleux;

import java.util.Collections;
import java.util.Map;

import org.fuwjin.postage.type.Optional;

/**
 * Base utility class for all things transformative.
 */
public abstract class Transformer implements Expression {
   /**
    * Parses the input stream.
    * @param input the input stream
    * @return the result
    */
   public Object transform(final InStream input) {
      return transform(input, OutStream.NONE, Collections.<String, Object> emptyMap());
   }

   /**
    * Parses the input stream.
    * @param input the input stream
    * @param scope the initial scope
    * @return the result
    */
   public Object transform(final InStream input, final Map<String, ? extends Object> scope) {
      return transform(input, OutStream.NONE, scope);
   }

   /**
    * Transforms the input, output, and scope into the result.
    * @param input the input stream
    * @param output the output stream
    * @param scope the scope
    * @return the result
    */
   public Object transform(final InStream input, final OutStream output, final Map<String, ? extends Object> scope) {
      final State state = transform(new StateImpl(input.start(), output.start(), new Scope(scope), Optional.UNSET));
      return state.value();
   }
}
