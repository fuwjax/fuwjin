package org.fuwjin.milik.test.ex;

import org.fuwjin.milik.Pausable;

public class ExPausable {
    void noop() throws Pausable {
        
    }
    
    void simple() throws Pausable {
        noop();
    }
}
