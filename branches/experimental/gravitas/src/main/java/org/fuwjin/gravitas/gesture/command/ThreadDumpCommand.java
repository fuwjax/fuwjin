package org.fuwjin.gravitas.gesture.command;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.fuwjin.gravitas.engine.Command;

public class ThreadDumpCommand extends Command{
   @Override
   public void doRun() throws Exception{
      StringBuilder builder = new StringBuilder();
      ThreadMXBean bean = ManagementFactory.getThreadMXBean();
      for(ThreadInfo info: bean.dumpAllThreads(true, true)){
         builder.append(info.toString());
      }
      source().send(builder);
   }
}
