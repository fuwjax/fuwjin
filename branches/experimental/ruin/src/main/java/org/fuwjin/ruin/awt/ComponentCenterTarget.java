package org.fuwjin.ruin.awt;

import java.awt.Component;

public class ComponentCenterTarget extends AbstractAwtTarget<Component> {
   public ComponentCenterTarget(final Component component) {
      super(component);
   }

   @Override
   public int getScreenX() {
      return component().getLocationOnScreen().x + component().getWidth() / 2;
   }

   @Override
   public int getScreenY() {
      return component().getLocationOnScreen().y + component().getHeight() / 2;
   }
}
