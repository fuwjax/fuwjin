package org.fuwjax.test;

import static org.fuwjax.minesweeper.gui.TextGame.newGame;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjax.minesweeper.gui.TextGame;
import org.junit.Test;

public class SpecificBoardTest{
   private final TextGame game = newGame(3, 3, 0, 8);

   @Test
   public void testTimer() throws InterruptedException{
      Thread.sleep(75);
      game.uncover(0, 2);
      Thread.sleep(75);
      assertThat(game.render(), is("2  ?  1 \n - 1 0 \n - 2 1 \n - - -"));
      game.uncover(1, 0);
      Thread.sleep(30);
      assertThat(game.render(), is("2  ?  2 \n - 1 0 \n 1 2 1 \n - - -"));
   }
   
   @Test
   public void testFlagMine(){
      game.flag(0, 0);
      assertThat(game.render(), is("1  ?  0 \n P - - \n - - - \n - - -"));
   }

   @Test
   public void testUncoverBlank(){
      game.uncover(0, 2);
      assertThat(game.render(), is("2  ?  0 \n - 1 0 \n - 2 1 \n - - -"));
   }

   @Test
   public void testUncoverMine(){
      game.uncover(2, 2);
      assertThat(game.render(), is("2  X  0 \n - - - \n - - - \n - - *"));
   }

   @Test
   public void testUncoverNumber(){
      game.uncover(1, 2);
      assertThat(game.render(), is("2  ?  0 \n - - - \n - - 1 \n - - -"));
   }
}
