package org.fuwjin.ruin.awt;

import java.awt.Component;
import java.util.concurrent.TimeoutException;
import org.fuwjin.ruin.Target;

public abstract class AbstractAwtTarget<C extends Component> implements Target {
   private final C c;

   protected AbstractAwtTarget(final C component) {
      c = component;
   }

   @Override
   public void await(final long millis) throws InterruptedException, TimeoutException {
      final long stop = System.currentTimeMillis() + millis;
      while(!c.isShowing() && System.currentTimeMillis() < stop) {
         Thread.sleep(100);
      }
      if(System.currentTimeMillis() >= stop) {
         throw new TimeoutException();
      }
   }

   protected C component() {
      return c;
   }
}
