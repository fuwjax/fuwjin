package org.fuwjax.minesweeper;

import static java.util.Arrays.deepToString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board{
   private static int[] randomSet(final int count, final int max){
      final Set<Integer> set = new HashSet<Integer>();
      final Random random = new Random();
      while(set.size() < count){
         set.add(random.nextInt(max));
      }
      final int[] ret = new int[count];
      int i = 0;
      for(final int value: set){
         ret[i++] = value;
      }
      return ret;
   }

   private Cell[][] cells;

   public void flag(final Game game, final int x, final int y){
      cells[x][y].flag(game);
   }

   public void reset(final GameConfig config){
      reset(config, randomSet(config.mines(), config.size()));
   }

   public void reset(final GameConfig config, final int... mineLocations){
      final Cell[] previousRow = new Cell[config.rows() + 2];
      Arrays.fill(previousRow, Cell.NULL);
      cells = new Cell[config.rows()][config.columns()];
      for(int row = 0; row < config.rows(); row++){
         Cell previousCell = Cell.NULL;
         for(int col = 0; col < config.columns(); col++){
            final Cell cell = new Cell(row, col);
            previousCell.linkNeighbor(cell);
            previousRow[col].linkNeighbor(cell);
            previousRow[col + 1].linkNeighbor(cell);
            previousRow[col + 2].linkNeighbor(cell);
            cells[row][col] = cell;
            previousCell = cell;
         }
         System.arraycopy(cells[row], 0, previousRow, 1, config.columns());
      }
      for(final int mine: mineLocations){
         cells[mine / config.columns()][mine % config.columns()].placeMine();
      }
   }

   @Override
   public String toString(){
      return deepToString(cells);
   }

   public void uncover(final Game game, final int x, final int y){
      cells[x][y].uncover(game);
   }
}
