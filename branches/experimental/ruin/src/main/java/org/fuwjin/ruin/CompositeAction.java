package org.fuwjin.ruin;

import java.awt.Robot;
import java.util.List;

public class CompositeAction implements RobotAction{
   public static RobotAction newComposite(final List<RobotAction> actions){
      if(actions.size() == 1){
         return actions.get(0);
      }
      return new CompositeAction(actions.toArray(new RobotAction[actions.size()]));
   }

   private final RobotAction[] strokes;

   public CompositeAction(final RobotAction... strokes){
      this.strokes = strokes;
   }

   @Override
   public void finish(final Robot robot){
      for(final RobotAction stroke: strokes){
         stroke.finish(robot);
      }
   }

   @Override
   public void perform(final Robot robot){
      for(final RobotAction stroke: strokes){
         stroke.perform(robot);
      }
   }

   @Override
   public void start(final Robot robot){
      for(final RobotAction stroke: strokes){
         stroke.start(robot);
      }
   }
}
