package org.fuwjin.ruin;

import java.util.concurrent.TimeoutException;

public interface Target {
   void await(long millis) throws InterruptedException, TimeoutException;

   int getScreenX();

   int getScreenY();
}
