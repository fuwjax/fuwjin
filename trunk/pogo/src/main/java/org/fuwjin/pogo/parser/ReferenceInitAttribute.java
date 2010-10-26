package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.Attribute;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.type.Optional;

public class ReferenceInitAttribute implements Parser, Attribute {
   private final String name;
   private Function function;
   private Parser parser;

   public ReferenceInitAttribute(final String name) {
      this.name = name;
   }

   @Override
   public Parser decorate(final Parser target) {
      parser = target;
      return this;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final ReferenceInitAttribute o = (ReferenceInitAttribute)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name);
   }

   @Override
   public boolean parse(final PogoState state) {
      final Object result = function.invokeSafe(Optional.UNSET, state.getValue());
      if(result instanceof Failure) {
         state.fail("could not invoke " + name, (Failure)result);
         return false;
      }
      state.setValue(result);
      return parser.parse(state);
   }

   @Override
   public void resolve(final Grammar grammar, final String namespace) {
      function = grammar.getFunction(namespace, name, void.class, Optional.OBJECT);
      parser.resolve(grammar, namespace);
   }

   @Override
   public String toString() {
      return "~" + name;
   }
}
