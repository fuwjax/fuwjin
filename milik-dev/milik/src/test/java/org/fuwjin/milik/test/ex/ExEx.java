package org.fuwjin.milik.test.ex;

import org.fuwjin.milik.Pausable;

public class ExEx {
    void noop(int i) throws Pausable {}
    
    void f() throws Pausable {
        int i = 0;
        try {
            noop(i);
        } catch (Exception e) {
            noop(i);
        }
        
    }
}
