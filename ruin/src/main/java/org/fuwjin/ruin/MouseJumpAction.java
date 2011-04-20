package org.fuwjin.ruin;

import java.awt.Robot;

public class MouseJumpAction implements RobotAction{
   private final Target target;

   public MouseJumpAction(final Target target){
      this.target = target;
   }

   @Override
   public void finish(final Robot robot){
      robot.mouseMove(target.getScreenX(), target.getScreenY());
   }

   @Override
   public void perform(final Robot robot){
      robot.mouseMove(target.getScreenX(), target.getScreenY());
   }

   @Override
   public void start(final Robot robot){
      robot.mouseMove(target.getScreenX(), target.getScreenY());
   }
}
