package org.fuwjin.chessur;

/**
 * Accepts based on a filter.
 */
public class FilterAcceptStatement implements Expression {
   private final boolean isNot;
   private final Filter filter;

   /**
    * Creates a new instance.
    * @param isNot if the result should be reversed
    * @param filter the filter to apply
    */
   public FilterAcceptStatement(final boolean isNot, final Filter filter) {
      this.isNot = isNot;
      this.filter = filter;
   }

   @Override
   public String toString() {
      return "accept " + (isNot ? "not " : "") + "in " + filter;
   }

   @Override
   public State transform(final State state) {
      if(filter.allow(state.current()) ^ isNot) {
         return state.accept();
      }
      return state.failure("Did not match filter: %s", filter);
   }
}
