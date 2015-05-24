package org.fuwjax.minesweeper;

public enum CellCover{
   PLAIN{
      @Override
      public CellCover flag(final Cell cell, final Game game){
         game.flagged(cell);
         return FLAGGED;
      }

      @Override
      public String toString(){
         return "?";
      }

      @Override
      public void uncover(final Cell cell, final Game game){
         cell.reveal(game);
      }
   },
   FLAGGED{
      @Override
      public CellCover flag(final Cell cell, final Game game){
         game.unflagged(cell);
         return PLAIN;
      }

      @Override
      public String toString(){
         return "P";
      }

      @Override
      public void uncover(final Cell cell, final Game game){
         // a flagged cell can't be uncovered
      }
   },
   NONE{
      @Override
      public CellCover flag(final Cell cell, final Game game){
         return NONE;
      }

      @Override
      public String toString(){
         return " ";
      }

      @Override
      public void uncover(final Cell cell, final Game game){
         // already uncovered
      }
   },
   ;
   public abstract CellCover flag(Cell cell, Game game);

   public abstract void uncover(Cell cell, Game game);
}
