package org.fuwjin.ruin;

import java.awt.Robot;

public class KeyAction implements RobotAction{
   private final int keycode;

   public KeyAction(final int keycode){
      this.keycode = keycode;
   }

   @Override
   public void finish(final Robot robot){
      robot.keyRelease(keycode);
   }

   @Override
   public void perform(final Robot robot){
      start(robot);
      finish(robot);
   }

   @Override
   public void start(final Robot robot){
      robot.keyPress(keycode);
   }
}