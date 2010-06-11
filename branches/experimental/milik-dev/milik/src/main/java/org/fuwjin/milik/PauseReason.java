/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik;

/**
 * @see Task#pause(PauseReason)
 */
public interface PauseReason {
    /**
     * True if the given task's reason for pausing is still valid. 
     */
    boolean isValid(Task t);
}
