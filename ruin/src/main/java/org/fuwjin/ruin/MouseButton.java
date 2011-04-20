package org.fuwjin.ruin;

import java.awt.Robot;
import java.awt.event.InputEvent;

public enum MouseButton implements RobotAction{
   LEFT(InputEvent.BUTTON1_MASK), RIGHT(InputEvent.BUTTON2_MASK), MIDDLE(InputEvent.BUTTON3_MASK);
   private int id;

   private MouseButton(final int id){
      this.id = id;
   }

   @Override
   public void finish(final Robot robot){
      robot.mouseRelease(id);
   }

   @Override
   public void perform(final Robot robot){
      start(robot);
      finish(robot);
   }

   @Override
   public void start(final Robot robot){
      robot.mousePress(id);
   }
}