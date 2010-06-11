/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.tools;

// Various print routines to call from jvml files (*.j). More convenient
// than calling System.out.println.

public class P {
  // Call as /org/fuwjin/milik/tools/P/pi(I)V
  public static void pi(int i) {
    System.out.println(i);
  }

  // Call as /org/fuwjin/milik/tools/P/pn()V
  public static void pn() {
    System.out.println();
  }

  // Call as /org/fuwjin/milik/tools/P/pn(Ljava/lang/Object;)V
  public static void pn(Object o) {
    System.out.println(o);
  }

  // Call as /org/fuwjin/milik/tools/P/p(Ljava/lang/Object;)V
  public static void p(Object o) {
    System.out.print(o);
  }

  // Call as /org/fuwjin/milik/tools/P/ps(Ljava/lang/Object;)V
  public static void ps(Object o) {
    System.out.print(o);
    System.out.print(" ");
  }
}
