package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Map;
import java.util.NoSuchElementException;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Pogo;
import org.fuwjin.pogo.PredefinedGrammar;
import org.fuwjin.pogo.reflect.FinalizerTask;
import org.fuwjin.pogo.reflect.InitializerTask;
import org.fuwjin.pogo.reflect.NullTask;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * Matches a rule indirectly and optionally persists the result of the rule.
 */
public class RuleReferenceParser implements Parser {
   private static final String UNKNOWN_RULE = "No rule named %s in grammar"; //$NON-NLS-1$ 
   private static final String REFERENCE = "Reference"; //$NON-NLS-1$
   private String ruleName;
   private Parser rule;
   private InitializerTask constructor = new NullTask();
   private FinalizerTask converter = new NullTask();
   private static Pogo serial;

   /**
    * Creates a new instance.
    * @param ruleName the name of the referred rule
    * @param initializer the initializer for the reference
    * @param finalizer the finalizer for the reference
    */
   public RuleReferenceParser(final String ruleName, final InitializerTask initializer, final FinalizerTask finalizer) {
      this.ruleName = ruleName;
      constructor = initializer;
      converter = finalizer;
   }

   /**
    * Creates a new instance.
    */
   RuleReferenceParser() {
      // for reflection
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final RuleReferenceParser o = (RuleReferenceParser)obj;
         return eq(getClass(), o.getClass()) && eq(ruleName, o.ruleName) && eq(constructor, o.constructor)
               && eq(converter, o.converter);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), ruleName, constructor, converter);
   }

   @Override
   public void parse(final PogoContext context) {
      final PogoContext child = constructor.initialize(context);
      if(child.isSuccess()) {
         rule.parse(child);
         if(child.isSuccess()) {
            converter.finalize(context, child);
         }
      }
   }

   @Override
   public void resolve(final Map<String, Parser> grammar, final ReflectionType ruleType) {
      rule = grammar.get(ruleName);
      if(rule == null) {
         throw new NoSuchElementException(String.format(UNKNOWN_RULE, ruleName));
      }
      constructor.setType(ruleType);
      converter.setType(ruleType);
   }

   @Override
   public String toString() {
      if(serial == null) {
         serial = PredefinedGrammar.PogoSerial.grammar().get(REFERENCE);
      }
      return serial.serial(this);
   }
}
