package org.fuwjin.ruin;

import java.awt.Robot;

public class DelayAction implements RobotAction{
   private final int millis;

   public DelayAction(final int millis){
      this.millis = millis;
   }

   @Override
   public void finish(final Robot robot){
      robot.delay(millis / 2);
   }

   @Override
   public void perform(final Robot robot){
      robot.delay(millis);
   }

   @Override
   public void start(final Robot robot){
      robot.delay(millis / 2);
   }
}
