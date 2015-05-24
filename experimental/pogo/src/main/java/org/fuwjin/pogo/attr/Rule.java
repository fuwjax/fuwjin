package org.fuwjin.pogo.attr;

/**
 * Manages a named expression.
 */
public class Rule {
   private Expression expression;
   private final String name;

   /**
    * Creates a new instance.
    * @param name the name of the rule
    */
   public Rule(final String name) {
      this.name = name;
   }

   /**
    * Returns the expression.
    * @return the expression
    */
   public Expression expression() {
      return expression;
   }

   /**
    * Sets this rule's expression.
    * @param newExpression the new expression
    */
   public void expression(final Expression newExpression) {
      if(expression != null) {
         throw new IllegalStateException("The rule " + name + " has already been assigned an expression");
      }
      expression = newExpression;
   }

   /**
    * Returns the name.
    * @return the name
    */
   public String name() {
      return name;
   }
}
