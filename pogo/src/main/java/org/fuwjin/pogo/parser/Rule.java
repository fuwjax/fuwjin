package org.fuwjin.pogo.parser;

import static org.fuwjin.util.ObjectUtils.eq;
import static org.fuwjin.util.ObjectUtils.hash;

import java.util.Map;

import org.fuwjin.io.PogoContext;
import org.fuwjin.pogo.Parser;
import org.fuwjin.pogo.Pogo;
import org.fuwjin.pogo.PredefinedGrammar;
import org.fuwjin.pogo.reflect.DefaultResultTask;
import org.fuwjin.pogo.reflect.FinalizerTask;
import org.fuwjin.pogo.reflect.InitializerTask;
import org.fuwjin.pogo.reflect.ObjectType;
import org.fuwjin.pogo.reflect.ReferenceTask;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * A grammar rule.
 */
public class Rule implements Parser {
   private static final String DEFINITION = "Definition"; //$NON-NLS-1$
   private static Pogo serial;
   private String name;
   private Parser parser;
   private FinalizerTask finalizer = new DefaultResultTask();
   private ReflectionType type = new ObjectType();
   private InitializerTask initializer = new ReferenceTask();

   /**
    * Creates a new instance.
    * @param name the name
    * @param type the bound type
    * @param initializer the rule initializer
    * @param finalizer the rule finalizer
    * @param parser the expression
    */
   public Rule(final String name, final ReflectionType type, final InitializerTask initializer,
         final FinalizerTask finalizer, final Parser parser) {
      this.name = name;
      this.type = type;
      this.initializer = initializer;
      this.finalizer = finalizer;
      this.parser = parser;
   }

   /**
    * Creates a new instance.
    */
   Rule() {
      // for reflection
   }

   @Override
   public boolean equals(final Object obj) {
      try {
         final Rule o = (Rule)obj;
         return eq(getClass(), o.getClass()) && eq(name, o.name) && eq(type, o.type) && eq(initializer, o.initializer)
               && eq(finalizer, o.finalizer) && eq(parser, o.parser);
      } catch(final ClassCastException e) {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return hash(getClass(), name, type, initializer, finalizer, parser);
   }

   /**
    * Returns the name.
    * @return the name
    */
   public String name() {
      return name;
   }

   @Override
   public void parse(final PogoContext context) {
      final PogoContext child = initializer.initialize(context);
      if(child.isSuccess()) {
         parser.parse(child);
         if(child.isSuccess()) {
            finalizer.finalize(context, child);
         }
      }
   }

   /**
    * Resolves this rule and the inner expression.
    * @param grammar the grammar to resolve rule references
    */
   public void resolve(final Map<String, Parser> grammar, final ReflectionType ignore) {
      initializer.setType(type);
      finalizer.setType(type);
      parser.resolve(grammar, type);
   }

   @Override
   public String toString() {
      if(serial == null) {
         serial = PredefinedGrammar.PogoSerial.grammar().get(DEFINITION);
      }
      return serial.serial(this);
   }
}
