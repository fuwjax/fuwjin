package org.fuwjin.ruin;

import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

public class ActionScript implements RobotAction{
   private final List<RobotAction> actions = new ArrayList<RobotAction>();
   private final int millis;

   public ActionScript(final int millis){
      this.millis = millis;
   }

   protected void addAction(final RobotAction action){
      actions.add(action);
   }

   @Override
   public void finish(final Robot robot){
      for(final RobotAction action: actions){
         action.finish(robot);
         robot.delay(millis);
      }
   }

   @Override
   public void perform(final Robot robot){
      for(final RobotAction action: actions){
         action.perform(robot);
         robot.delay(millis);
      }
   }

   @Override
   public void start(final Robot robot){
      for(final RobotAction action: actions){
         action.start(robot);
         robot.delay(millis);
      }
   }
}
