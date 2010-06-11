package org.fuwjin.milik.test.ex;

public interface TaskStatusCB {
    void beforeYield();
    void afterYield();
    void done();
}
