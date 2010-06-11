/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.test;

import junit.framework.TestCase;

import org.fuwjin.milik.ExitMsg;
import org.fuwjin.milik.Mailbox;
import org.fuwjin.milik.Scheduler;
import org.fuwjin.milik.Task;
import org.fuwjin.milik.test.ex.ExYieldBase;

public class TestGenerics extends TestCase {

  public void testGenerics() throws Exception {
    ExYieldBase task;

    task = (ExYieldBase) (Class.forName("org.fuwjin.milik.test.ex.ExGenerics").newInstance());
    runTask(task);
  }

  public static void runTask(Task task) throws Exception {
    Mailbox<ExitMsg> exitmb = new Mailbox<ExitMsg>();
    Scheduler s = new Scheduler(1);
    task.informOnExit(exitmb);
    task.setScheduler(s);
    task.start();

    ExitMsg m = exitmb.getb();
    if (m == null) {
      fail("Timed Out");
    } else {
      Object res = m.result;
      if (res instanceof Throwable) {
        ((Throwable) res).printStackTrace();
        fail(m.toString());
      }
    }
    s.shutdown();
  }
}
