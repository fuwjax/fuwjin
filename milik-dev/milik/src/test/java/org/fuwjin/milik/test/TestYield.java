/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.test;

import java.util.Timer;

import org.fuwjin.milik.ExitMsg;
import org.fuwjin.milik.Mailbox;
import org.fuwjin.milik.Scheduler;
import org.fuwjin.milik.Task;
import org.fuwjin.milik.test.ex.ExYieldBase;

import junit.framework.TestCase;

public class TestYield extends TestCase {
    Timer               timer = new Timer();
    
    public void testStackBottom_st() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldStack(0));
    }

    public void testStackBottom_v() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldStack(1));
    }
    
    public void testStackBottom_av() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldStack(2));
    }
    
    public void testFactorial_st() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldStack(3));
    }

    public void testFactorial_av() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldStack(4));
    }
    
    public void testDupsInVars() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldDups(0));
    }

    public void testDupsInStack() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldDups(1));
    }

    public void testConstantsInStack() throws Exception {
        runTask(new org.fuwjin.milik.test.ex.ExYieldConstants(0));
    }

    public static void runTask(String taskClassName, int testCase) throws Exception {
        ExYieldBase task;
        
        task = (ExYieldBase) (Class.forName(taskClassName).newInstance());
        task.testCase = testCase;
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
                ((Throwable)res).printStackTrace();
                fail(m.toString());
            }
        }
        s.shutdown();
    }
}
