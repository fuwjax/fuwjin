package org.fuwjax.minesweeper.gui;

import static org.fuwjax.minesweeper.gui.CellState.BLANK_CELL;
import static org.fuwjax.minesweeper.gui.CellState.FLAG_COVER;
import static org.fuwjax.minesweeper.gui.CellState.MINE;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_1;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_2;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_3;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_4;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_5;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_6;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_7;
import static org.fuwjax.minesweeper.gui.CellState.NUMBER_8;
import static org.fuwjax.minesweeper.gui.CellState.PLAIN_COVER;
import static org.fuwjax.minesweeper.gui.GameState.GAME_LOST;
import static org.fuwjax.minesweeper.gui.GameState.GAME_WON;
import static org.fuwjax.minesweeper.gui.GameState.IN_PROGRESS;

import java.util.EnumMap;
import java.util.Map;

import org.fuwjax.minesweeper.Cell;
import org.fuwjax.minesweeper.GameConfig;

public class TextUserInterface implements GameUserInterface{
   private static Map<CellState, String> stateStrings = new EnumMap<CellState, String>(CellState.class);
   private static Map<GameState, String> gameStateStrings = new EnumMap<GameState, String>(GameState.class);
   static{
      stateStrings.put(BLANK_CELL, "0");
      stateStrings.put(NUMBER_1, "1");
      stateStrings.put(NUMBER_2, "2");
      stateStrings.put(NUMBER_3, "3");
      stateStrings.put(NUMBER_4, "4");
      stateStrings.put(NUMBER_5, "5");
      stateStrings.put(NUMBER_6, "6");
      stateStrings.put(NUMBER_7, "7");
      stateStrings.put(NUMBER_8, "8");
      stateStrings.put(MINE, "*");
      stateStrings.put(PLAIN_COVER, "-");
      stateStrings.put(FLAG_COVER, "P");
      gameStateStrings.put(IN_PROGRESS, "?");
      gameStateStrings.put(GAME_LOST, "X");
      gameStateStrings.put(GAME_WON, "!");
   }
   private CellState[][] cells;
   private GameState state = IN_PROGRESS;
   private int score;
   private int flags;

   @Override
   public void cellStateChanged(final Cell cell, final CellState state){
      cells[cell.row()][cell.column()] = state;
   }

   @Override
   public void flagsRemainingChanged(final int flagsRemaining){
      flags = flagsRemaining > 0 ? flagsRemaining : 0;
   }

   @Override
   public void gameStateChanged(final GameState state){
      this.state = state;
   }

   public void newGameInitialized(final GameConfig config){
      cells = new CellState[config.rows()][config.columns()];
      for(int row = 0; row < config.rows(); row++){
         for(int col = 0; col < config.columns(); col++){
            cells[row][col] = PLAIN_COVER;
         }
      }
      score = 0;
      flags = config.mines();
   }

   public String render(){
      final StringBuilder builder = new StringBuilder();
      builder.append(flags).append("  ").append(gameStateStrings.get(state)).append("  ").append(score);
      for(final CellState[] cell: cells){
         builder.append(" \n");
         for(int j = 0; j < cell.length; j++){
            builder.append(' ').append(stateStrings.get(cell[j]));
         }
      }
      return builder.toString();
   }

   @Override
   public void timerChanged(final int time){
      score = time;
   }
}
