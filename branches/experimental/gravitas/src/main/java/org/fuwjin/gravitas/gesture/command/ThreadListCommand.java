package org.fuwjin.gravitas.gesture.command;

import static java.util.Arrays.sort;
import static org.fuwjin.util.StringUtils.join;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Comparator;

import org.fuwjin.gravitas.engine.Command;

public class ThreadListCommand extends Command{
   private static final Comparator<ThreadInfo> idOrder = new Comparator<ThreadInfo>(){
      @Override
      public int compare(ThreadInfo o1, ThreadInfo o2){
         return (int)(o1.getThreadId()-o2.getThreadId());
      }
   };

   @Override
   public void doRun() throws Exception{
      final StringBuilder builder = new StringBuilder();
      final ThreadMXBean bean = ManagementFactory.getThreadMXBean();
      Object sep = join("\n");
      ThreadInfo[] threads = bean.dumpAllThreads(true, true);
      sort(threads, idOrder);
      for(final ThreadInfo info: threads){
         builder.append(sep).append(info.getThreadId()).append(") [").append(info.getThreadState())
         .append("] ").append(info.getThreadName());
      }
      source().send(builder);
   }
}
