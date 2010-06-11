/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.examples;

import org.fuwjin.milik.Pausable;
import org.fuwjin.milik.Task;
import org.fuwjin.milik.TaskGroup;

public class Group {
    public static void main(String[] args) {
        TaskGroup tg = new TaskGroup();
        tg.add(new GroupTask().start());
        tg.add(new GroupTask().start());
        tg.joinb();
        System.exit(0);
    }
    
    static class GroupTask extends Task {
        public void execute() throws Pausable {
            System.out.println("Task #" + id + "sleeping");
            Task.sleep(1000);
            System.out.println("Task #" + id + "done");
        }
    }
}