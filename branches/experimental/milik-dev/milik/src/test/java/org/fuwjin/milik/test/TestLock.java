package org.fuwjin.milik.test;

import org.fuwjin.milik.ExitMsg;
import org.fuwjin.milik.Mailbox;
import org.fuwjin.milik.Pausable;
import org.fuwjin.milik.ReentrantLock;
import org.fuwjin.milik.Scheduler;
import org.fuwjin.milik.Task;

import junit.framework.TestCase;

public class TestLock extends TestCase{
    public void testLocks() {
        Scheduler scheduler = new Scheduler(4);
        Mailbox<ExitMsg> mb = new Mailbox<ExitMsg>();
        for (int i = 0; i < 100; i++) {
            Task t = new LockTask();
            t.informOnExit(mb);
            t.setScheduler(scheduler);
            t.start();
        }
        boolean ok = true;
        for (int i = 0; i < 100; i++) {
            ExitMsg em = mb.getb(5000);
            assertNotNull("Timed out. #tasks finished = " + i + "/100", em);
            if (em.result instanceof Exception) {
                ok = false; break;
            }
        }
        scheduler.shutdown();
        assertTrue(ok);
    }
    
    static class LockTask extends Task {
        ReentrantLock syncLock = new ReentrantLock();
        @Override
        public void execute() throws Pausable, Exception {
//            System.out.println("Start #" + id);
            try {
            for (int i = 0; i < 1000; i++) {
                syncLock.lock();
                Task.yield();
                syncLock.unlock();
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println("Done #" + id);
        }
    }
}
