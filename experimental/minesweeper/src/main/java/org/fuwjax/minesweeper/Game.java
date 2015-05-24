package org.fuwjax.minesweeper;

import static org.fuwjax.minesweeper.gui.CellState.BLANK_CELL;
import static org.fuwjax.minesweeper.gui.CellState.FLAG_COVER;
import static org.fuwjax.minesweeper.gui.CellState.MINE;
import static org.fuwjax.minesweeper.gui.CellState.PLAIN_COVER;
import static org.fuwjax.minesweeper.gui.CellState.values;
import static org.fuwjax.minesweeper.gui.GameState.GAME_LOST;
import static org.fuwjax.minesweeper.gui.GameState.GAME_WON;
import static org.fuwjax.minesweeper.gui.GameState.IN_PROGRESS;

import java.util.Timer;

import org.fuwjax.minesweeper.gui.CellState;
import org.fuwjax.minesweeper.gui.GameUserInterface;

public class Game{
   private int flagsPlaced;
   private int revealed;
   private final Board board;
   private final GameUserInterface gui;
   private final GameTimer timer;
   private GameConfig config;

   public Game(final Board board, final GameUserInterface gui){
      this.board = board;
      this.gui = gui;
      timer = new GameTimer(gui, new Timer(true));
   }

   public void flag(final int x, final int y){
      board.flag(this, x, y);
   }

   public void flagged(final Cell cell){
      gui.cellStateChanged(cell, FLAG_COVER);
      flagsPlaced++;
      gui.flagsRemainingChanged(config.mines() - flagsPlaced);
   }

   public void newGame(final GameConfig config){
      board.reset(config);
      gui.newGameInitialized(config);
      this.config = config;
      revealed = 0;
      flagsPlaced = 0;
      gui.gameStateChanged(IN_PROGRESS);
      timer.reset(config);
   }

   public void newGame(final GameConfig config, final int... mineLocations){
      board.reset(config, mineLocations);
      gui.newGameInitialized(config);
      this.config = config;
      revealed = 0;
      flagsPlaced = 0;
      gui.gameStateChanged(IN_PROGRESS);
      timer.reset(config);
   }

   public void revealBlank(final Cell cell){
      reveal(cell, BLANK_CELL);
   }

   public void revealMine(final Cell cell){
      gui.cellStateChanged(cell, MINE);
      gui.gameStateChanged(GAME_LOST);
      timer.stop();
   }

   public void revealNumber(final Cell cell){
      reveal(cell, values()[cell.neighborMines()]);
   }

   @Override
   public String toString(){
      return "(" + flagsPlaced + "," + revealed + ")" + board;
   }

   public void uncover(final int x, final int y){
      board.uncover(this, x, y);
   }

   public void unflagged(final Cell cell){
      gui.cellStateChanged(cell, PLAIN_COVER);
      flagsPlaced--;
      gui.flagsRemainingChanged(config.mines() - flagsPlaced);
   }

   private void reveal(final Cell cell, final CellState state){
      gui.cellStateChanged(cell, state);
      if(revealed == 0){
         timer.start();
      }
      revealed++;
      if(revealed == config.revealToWin()){
         timer.stop();
         gui.gameStateChanged(GAME_WON);
      }
   }
}
