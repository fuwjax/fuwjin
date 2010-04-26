package org.fuwjax.minesweeper;

public enum CellContent{
   NUMBER{
      @Override
      public CellContent nextToMine(){
         return NUMBER;
      }

      @Override
      public void reveal(final Cell cell, final Game game){
         game.revealNumber(cell);
      }

      @Override
      public String toString(){
         return "#";
      }
   },
   MINE{
      @Override
      public CellContent nextToMine(){
         return MINE;
      }

      @Override
      public void reveal(final Cell cell, final Game game){
         game.revealMine(cell);
      }

      @Override
      public String toString(){
         return "*";
      }
   },
   BLANK{
      @Override
      public CellContent nextToMine(){
         return NUMBER;
      }

      @Override
      public void reveal(final Cell cell, final Game game){
         game.revealBlank(cell);
         cell.uncoverNeighbors(game);
      }

      @Override
      public String toString(){
         return "_";
      }
   };
   public abstract CellContent nextToMine();

   public abstract void reveal(Cell cell, Game game);
}
