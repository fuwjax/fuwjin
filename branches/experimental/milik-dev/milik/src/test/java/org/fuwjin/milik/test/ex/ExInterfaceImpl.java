package org.fuwjin.milik.test.ex;

import org.fuwjin.milik.*;

interface ExInterface {
    int foo(float f) throws Pausable;
}

public class ExInterfaceImpl extends Task implements ExInterface  {
    public void execute() throws Pausable {
        foo(10.2f);
    }
    
    public int foo(float f) throws Pausable {
        Task.sleep(1000);
        
        return (int)f;
    }
}
