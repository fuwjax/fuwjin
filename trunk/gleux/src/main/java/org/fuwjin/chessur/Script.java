package org.fuwjin.chessur;

/**
 * Represents a Specificaton reference.
 */
public class Script extends Transformer {
   private Declaration decl;
   private final String name;

   public Script(final String name) {
      this.name = name;
   }

   /**
    * Returns the declaration.
    * @return the declaration
    */
   public Declaration declaration() {
      return decl;
   }

   /**
    * Returns the name.
    * @return the name
    */
   public String name() {
      return name;
   }

   /**
    * Sets this specification's declaration.
    * @param decl the new declaration
    */
   protected void setDecl(final Declaration decl) {
      this.decl = decl;
   }

   @Override
   public String toString() {
      return "<" + name() + ">";
   }

   @Override
   public State transform(final State state) {
      if(decl == null) {
         throw new RuntimeException("Undefined script " + name);
      }
      final State result = decl.transform(state);
      if(result.isSuccess()) {
         return result;
      }
      return state.failure(result, "failed in %s %s", decl.name(), state);
   }
}
