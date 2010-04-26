package org.fuwjax.test;

import static org.fuwjax.minesweeper.gui.TextGame.newGame;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjax.minesweeper.gui.TextGame;
import org.junit.Test;

public class BoardWithNoMinesTest{
   private final TextGame game = newGame(3, 3, 0);

   @Test
   public void testBoardInit(){
      assertThat(game.render(), is("0  ?  0 \n - - - \n - - - \n - - -"));
   }

   @Test
   public void testFlag(){
      game.flag(1, 1);
      assertThat(game.render(), is("0  ?  0 \n - - - \n - P - \n - - -"));
   }

   @Test
   public void testFlagUncovered(){
      game.uncover(1, 1);
      game.flag(1, 1);
      assertThat(game.render(), is("0  !  0 \n 0 0 0 \n 0 0 0 \n 0 0 0"));
   }

   @Test
   public void testUncover(){
      game.uncover(2, 2);
      assertThat(game.render(), is("0  !  0 \n 0 0 0 \n 0 0 0 \n 0 0 0"));
   }

   @Test
   public void testUncoverFlag(){
      game.flag(1, 1);
      game.uncover(1, 1);
      assertThat(game.render(), is("0  ?  0 \n - - - \n - P - \n - - -"));
   }

   @Test
   public void testUnFlag(){
      game.flag(1, 1);
      game.flag(1, 1);
      assertThat(game.render(), is("0  ?  0 \n - - - \n - - - \n - - -"));
   }
}
