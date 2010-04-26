package org.fuwjax.minesweeper;

import static org.fuwjax.minesweeper.CellContent.BLANK;
import static org.fuwjax.minesweeper.CellContent.MINE;
import static org.fuwjax.minesweeper.CellCover.NONE;
import static org.fuwjax.minesweeper.CellCover.PLAIN;

import java.util.HashSet;
import java.util.Set;

public class Cell{
   public static final Cell NULL = new Cell(){
      @Override
      public void linkNeighbor(final Cell neighbor){
         // ignore
      }
   };
   private final Set<Cell> neighbors = new HashSet<Cell>();
   private CellCover cover = PLAIN;
   private CellContent reveal = BLANK;
   public int row;
   public int col;
   private int neighborMines;

   public Cell(final int row, final int col){
      this.row = row;
      this.col = col;
   }

   private Cell(){
      this(-1, -1);
   }

   public int column(){
      return col;
   }

   public void flag(final Game game){
      cover = cover.flag(this, game);
   }

   public void linkNeighbor(final Cell neighbor){
      neighbors.add(neighbor);
      neighbor.neighbors.add(this);
   }

   public int neighborMines(){
      return neighborMines;
   }

   public void placeMine(){
      reveal = MINE;
      for(final Cell neighbor: neighbors){
         neighbor.neighborMines++;
         neighbor.reveal = neighbor.reveal.nextToMine();
      }
   }

   public void reveal(final Game game){
      cover = NONE;
      reveal.reveal(this, game);
   }

   public int row(){
      return row;
   }

   @Override
   public String toString(){
      return "(" + row + "," + col + ")" + cover + reveal + neighborMines;
   }

   public void uncover(final Game game){
      cover.uncover(this, game);
   }

   public void uncoverNeighbors(final Game game){
      for(final Cell neighbor: neighbors){
         neighbor.uncover(game);
      }
   }
}
