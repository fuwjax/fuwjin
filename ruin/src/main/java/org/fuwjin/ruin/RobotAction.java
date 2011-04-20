package org.fuwjin.ruin;

import java.awt.Robot;

public interface RobotAction{
   void finish(Robot robot);

   void perform(Robot robot);

   void start(Robot robot);
}