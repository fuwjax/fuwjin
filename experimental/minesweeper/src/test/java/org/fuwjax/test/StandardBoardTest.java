package org.fuwjax.test;

import static org.fuwjax.minesweeper.gui.TextGame.newGame;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjax.minesweeper.gui.TextGame;
import org.junit.Test;

public class StandardBoardTest{
   private final TextGame game = newGame(10, 10, 10);

   @Test
   public void testBoardInit(){
      int count = 0;
      for(int row = 0; row < 10; row++){
         for(int col = 0; col < 10; col++){
            count += game.isMine(row, col) ? 1 : 0;
         }
      }
      assertThat(count, is(10));
   }
}
