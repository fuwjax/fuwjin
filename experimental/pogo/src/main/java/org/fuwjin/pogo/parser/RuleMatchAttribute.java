package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import org.fuwjin.pogo.Attribute;
import org.fuwjin.pogo.Grammar;
import org.fuwjin.pogo.ParsingExpression;
import org.fuwjin.pogo.state.PogoPosition;
import org.fuwjin.pogo.state.PogoState;
import org.fuwjin.postage.Failure;
import org.fuwjin.postage.Function;
import org.fuwjin.postage.type.Optional;

public class RuleMatchAttribute implements ParsingExpression, Attribute {
   final static RuleMatchAttribute RETURN = new RuleMatchAttribute("return");
   private final String name;
   private Function function;
   private ParsingExpression parser;

   public RuleMatchAttribute(final String name) {
      this.name = name;
   }

   @Override
   public ParsingExpression decorate(final ParsingExpression target) {
      parser = target;
      return this;
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final RuleMatchAttribute o = (RuleMatchAttribute)obj;
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
      final PogoPosition buffer = state.buffer(true);
      try {
         if(parser.parse(state)) {
            final Object result = function.invokeSafe(state.getValue(), buffer.toString());
            if(result instanceof Failure) {
               state.fail("could not handle rule match" + name, (Failure)result);
            } else {
               state.setValue(result);
               return true;
            }
         }
         buffer.reset();
         return false;
      } finally {
         buffer.release();
      }
   }

   @Override
   public void resolve(final Grammar grammar, final String namespace) {
      function = grammar.getFunction(namespace, name, Optional.OBJECT, String.class);
      parser.resolve(grammar, namespace);
   }

   @Override
   public String toString() {
      return ">" + name;
   }
}
