package org.fuwjin.chessur;

/**
 * Represents a Specification declaration.
 */
public class Declaration implements Expression {
   private final String name;
   private final Block block;
   private Expression returns;

   /**
    * Creates a new instance.
    * @param name the specification name
    */
   public Declaration(final String name) {
      this.name = name;
      block = new Block();
   }

   /**
    * Adds a statement to the declaration block.
    * @param statement the next statement
    */
   public void add(final Expression statement) {
      block.add(statement);
   }

   /**
    * Returns the specification name.
    * @return the specification name
    */
   public String name() {
      return name;
   }

   /**
    * Returns the declaration return value, or null if there is none.
    * @return the return value
    */
   public Expression returns() {
      return returns;
   }

   /**
    * Sets the return value.
    * @param value the new return value
    */
   public void returns(final Expression value) {
      returns = value;
   }

   /**
    * Returns the list of statements.
    * @return the list of statements
    */
   public Iterable<Expression> statements() {
      return block.statements();
   }

   @Override
   public String toString() {
      return "<" + name + ">" + block + " return " + returns;
   }

   @Override
   public State transform(final State state) {
      State result = block.transform(state.mark());
      if(result.isSuccess()) {
         if(returns != null) {
            result = returns.transform(result);
         }
         if(result.isSuccess()) {
            result = result.restoreScope(state);
         }
      }
      return result;
   }
}