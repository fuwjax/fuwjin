package org.fuwjax.minesweeper;

public class GameConfig{
   private final int mines;
   private final int rows;
   private final int columns;
   private final int tick;

   public GameConfig(final int rows, final int columns, final int mines, final int tick){
      this.mines = mines;
      this.rows = rows;
      this.columns = columns;
      this.tick = tick;
   }

   public int columns(){
      return columns;
   }

   public int mines(){
      return mines;
   }

   public int revealToWin(){
      return size() - mines();
   }

   public int rows(){
      return rows;
   }

   public int size(){
      return rows * columns;
   }

   public int tick(){
      return tick;
   }
}
