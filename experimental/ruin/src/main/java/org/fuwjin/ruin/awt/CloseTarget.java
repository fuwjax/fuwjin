package org.fuwjin.ruin.awt;

import java.awt.Window;

public class CloseTarget extends AbstractAwtTarget<Window> {
   public CloseTarget(final Window frame) {
      super(frame);
   }

   @Override
   public int getScreenX() {
      return component().getLocationOnScreen().x + 15;
   }

   @Override
   public int getScreenY() {
      return component().getLocationOnScreen().y + 10;
   }
}
