package org.fuwjin.ruin;

import java.awt.Robot;

public class ActionDecorator implements RobotAction{
   private final RobotAction inner;
   private final RobotAction outer;

   public ActionDecorator(final RobotAction inner, final RobotAction outer){
      this.inner = inner;
      this.outer = outer;
   }

   @Override
   public void finish(final Robot robot){
      inner.finish(robot);
      outer.finish(robot);
   }

   @Override
   public void perform(final Robot robot){
      outer.start(robot);
      inner.perform(robot);
      outer.finish(robot);
   }

   @Override
   public void start(final Robot robot){
      outer.start(robot);
      inner.start(robot);
   }
}