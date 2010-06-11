package org.fuwjin.milik.test.ex;

import org.fuwjin.milik.Pausable;

public class ExFlow {
  void loop() throws Pausable {
    ExA a = null;
    int i;
    for (i = 0; i < 10; i++) {
      if (i < 5) {
        a = new ExC();
      } else {
        a = new ExD();
        ;
      }
    }
    // at join, the stack must have types of
    // [I,Lorg.fuwjin.milik.test.ex.ExFlow; and Lorg.fuwjin.milik.test.ex.ExA;
    // local vars-> 0:Lorg.fuwjin.milik.test.ex.ExFlow;
    // 1:Lorg.fuwjin.milik.test.ex.ExA; 2:int 3:UNDEFINED
    int x = 10 * join(a);
    System.out.println(i);
    System.out.println(x);
  }

  int join(ExA a) throws Pausable {
    return 10;
  }
}