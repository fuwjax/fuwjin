/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.test;

import org.fuwjin.milik.ExitMsg;
import org.fuwjin.milik.Mailbox;
import org.fuwjin.milik.Scheduler;
import org.fuwjin.milik.Task;

import junit.framework.TestCase;

public class TestInterface extends TestCase {
    public void testIntCall() throws Exception {
        Task task = new org.fuwjin.milik.test.ex.ExInterfaceImpl();
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
