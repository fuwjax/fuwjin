package org.fuwjin.pogo.attr;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a root for file transformation.
 */
public class Grammar extends Machine {
   public static Attribute assign(final String name, final Attribute value) {
      return new AssignmentAttribute(name, value);
   }

   public static Expression lit(final String literal) {
      return new LiteralExpression(literal);
   }

   public static Attribute match() {
      return new MatchAttribute();
   }

   public static Expression result(final Rule rule, final Attribute value) {
      return new ReferenceExpression(rule);
   }

   private final Map<String, Rule> rules = new LinkedHashMap<String, Rule>();
   private Expression defaultExpression;

   /**
    * Adds a new rule.
    * @param name the name of the rule
    * @param expression the rule expression
    */
   public void add(final String name, final Expression expression) {
      if(defaultExpression == null) {
         defaultExpression = expression;
      }
      get(name).expression(expression);
   }

   @Override
   protected Expression expression() {
      return defaultExpression;
   }

   /**
    * Returns a named rule.
    * @param name the name of the rule
    * @return the rule
    */
   public Rule get(final String name) {
      Rule rule = rules.get(name);
      if(rule == null) {
         rule = new Rule(name);
         rules.put(name, rule);
      }
      return rule;
   }
}
