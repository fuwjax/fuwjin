package org.fuwjin.pogo.state;

public class ParseMemo extends AbstractMemo {
   private final ParseState state;
   private String buffer;
   private final int start;

   public ParseMemo(final String name, final ParseState state, final int start) {
      super(name, state);
      this.state = state;
      this.start = start;
   }

   @Override
   protected void restore() {
      if(buffer != null) {
         state.append(buffer);
      }
      super.restore();
   }

   @Override
   public boolean store() {
      if(!super.store()) {
         return false;
      }
      if(start >= 0) {
         buffer = state.substring(start);
      }
      return true;
   }
}
