package org.fuwjax.minesweeper.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.fuwjax.minesweeper.Board;
import org.fuwjax.minesweeper.Cell;
import org.fuwjax.minesweeper.Game;
import org.fuwjax.minesweeper.GameConfig;

public class TextGame extends Game{
   public static TextGame newGame(final int rows, final int cols, final int mines){
      final GameConfig config = new GameConfig(rows, cols, mines, 50);
      final Board board = new Board();
      final TextUserInterface gui = new TextUserInterface();
      final TextGame game = new TextGame(board, gui);
      game.newGame(config);
      return game;
   }

   public static TextGame newGame(final int rows, final int cols, final int... mineLocations){
      final GameConfig config = new GameConfig(rows, cols, mineLocations.length, 50);
      final Board board = new Board();
      final TextUserInterface gui = new TextUserInterface();
      final TextGame game = new TextGame(board, gui);
      game.newGame(config, mineLocations);
      return game;
   }

   private final TextUserInterface gui;
   private boolean lostGame;

   private TextGame(final Board board, final TextUserInterface gui){
      super(board, gui);
      this.gui = gui;
   }

   public boolean isMine(final int row, final int col){
      super.uncover(row, col);
      final boolean ret = lostGame;
      lostGame = false;
      return ret;
   }

   public String render(){
      return gui.render();
   }

   @Override
   public void revealMine(final Cell cell){
      super.revealMine(cell);
      lostGame = true;
   }
   
   public static void main(String... args) throws Exception{
      final GameConfig config = new GameConfig(10, 10, 10, 1000);
      final Board board = new Board();
      final TextUserInterface gui = new TextUserInterface();
      final TextGame game = new TextGame(board, gui);
      game.newGame(config);
      System.out.println(game.render());
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String line = reader.readLine();
      while(line != null){
         if("new".equalsIgnoreCase(line)){
            game.newGame(config);
         }else if(line.startsWith("f")){
            int pos = Integer.parseInt(line.substring(1));
            game.flag(pos/config.columns(), pos%config.columns());
         }else{
            int pos = Integer.parseInt(line);
            game.uncover(pos/config.columns(), pos%config.columns());
         }
         System.out.println(game.render());
         line = reader.readLine();
      }
   }
}
