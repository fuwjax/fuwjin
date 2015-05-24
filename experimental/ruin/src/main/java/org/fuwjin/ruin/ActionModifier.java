package org.fuwjin.ruin;

import java.awt.event.KeyEvent;

public enum ActionModifier{
   SHIFT(KeyEvent.VK_SHIFT);
   private RobotAction self;

   private ActionModifier(final int id){
      self = new KeyAction(id);
   }

   public RobotAction modified(final RobotAction action){
      return new ActionDecorator(action, self);
   }
}