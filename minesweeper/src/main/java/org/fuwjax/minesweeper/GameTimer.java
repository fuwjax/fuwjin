package org.fuwjax.minesweeper;

import java.util.Timer;
import java.util.TimerTask;

import org.fuwjax.minesweeper.gui.GameUserInterface;

public class GameTimer{
   private int time;
   private final Timer timer;
   private TimerTask task = newTask();
   private GameConfig config;
   private final GameUserInterface gui;

   public GameTimer(final GameUserInterface gui, final Timer timer){
      this.gui = gui;
      this.timer = timer;
   }

   public void reset(final GameConfig config){
      this.config = config;
      time = 0;
   }

   public void start(){
      timer.scheduleAtFixedRate(task, config.tick(), config.tick());
   }

   public void stop(){
      task.cancel();
      task = newTask();
   }

   private TimerTask newTask(){
      return new TimerTask(){
         @Override
         public void run(){
            gui.timerChanged(++time);
         }
      };
   }
}
