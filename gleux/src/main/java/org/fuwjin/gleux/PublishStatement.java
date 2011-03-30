package org.fuwjin.gleux;

/**
 * Publishes a value to an outstream.
 */
public class PublishStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the value to publish
    */
   public PublishStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return "publish " + value;
   }

   @Override
   public State transform(final State state) {
      final State result = value.transform(state);
      if(!result.isSuccess()) {
         return result;
      }
      return result.publish();
   }
}
