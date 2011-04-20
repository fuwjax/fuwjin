package org.fuwjin.ruin;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class MouseMoveAction implements RobotAction{
   private final Target target;

   public MouseMoveAction(final Target target){
      this.target = target;
   }

   @Override
   public void finish(final Robot robot){
      robot.mouseMove(target.getScreenX(), target.getScreenY());
   }

   @Override
   public void perform(final Robot robot){
      start(robot);
      finish(robot);
   }

   @Override
   public void start(final Robot robot){
      final Point current = MouseInfo.getPointerInfo().getLocation();
      final int dx = target.getScreenX() - current.x;
      final int dy = target.getScreenY() - current.y;
      final double dist = Math.sqrt(dx * dx + dy * dy);
      final double theta = Math.atan2(dy, dx);
      for(int i = 0; i < dist; i += 5){
         robot.mouseMove((int)(i * Math.cos(theta) + current.x), (int)(i * Math.sin(theta) + current.y));
         robot.delay(10);
      }
   }
}
