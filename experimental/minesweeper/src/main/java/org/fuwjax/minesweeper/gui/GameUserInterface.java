package org.fuwjax.minesweeper.gui;

import org.fuwjax.minesweeper.Cell;
import org.fuwjax.minesweeper.GameConfig;

public interface GameUserInterface{
   void cellStateChanged(Cell cell, CellState state);

   void flagsRemainingChanged(int flagsRemaining);

   void gameStateChanged(GameState state);

   void newGameInitialized(GameConfig config);

   void timerChanged(int time);
}
