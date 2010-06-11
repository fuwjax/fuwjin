/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.test;

import static org.fuwjin.milik.Constants.D_BYTE;

import org.fuwjin.milik.analysis.BasicBlock;
import org.fuwjin.milik.analysis.Frame;
import org.fuwjin.milik.analysis.IncompatibleTypesException;
import org.fuwjin.milik.analysis.MethodFlow;
import org.fuwjin.milik.analysis.TypeDesc;
import org.fuwjin.milik.analysis.Value;

public class TestFlow extends Base {

  @Override
  protected void setUp() throws Exception {
    cache("org.fuwjin.milik.test.ex.ExFlow");
  }

  public void testMerge() throws IncompatibleTypesException {
    MethodFlow flow = getFlow("loop");
    if (flow == null)
      return;
    // Make sure the merging is fine. There used to be a bug
    assertEquals("Lorg/fuwjin/milik/test/ex/ExA;", TypeDesc.mergeType("Lorg/fuwjin/milik/test/ex/ExC;",
        "Lorg/fuwjin/milik/test/ex/ExD;"));
    assertEquals("Lorg/fuwjin/milik/test/ex/ExA;", TypeDesc.mergeType("Lorg/fuwjin/milik/test/ex/ExD;",
        "Lorg/fuwjin/milik/test/ex/ExC;"));
    BasicBlock bb = getBBForMethod(flow, "join");
    assertTrue(bb != null);
    Frame f = bb.startFrame;
    // Check Locals
    // assertEquals("Lorg/fuwjin/milik/test/ex/ExFlow;", f.getLocal(0));
    assertEquals("Lorg/fuwjin/milik/test/ex/ExA;", f.getLocal(1).getTypeDesc());
    // assertSame(D_INT, f.getLocal(2));
    // Check operand stack
    assertSame(D_BYTE, f.getStack(0).getTypeDesc());
    assertEquals("Lorg/fuwjin/milik/test/ex/ExFlow;", f.getStack(1).getTypeDesc());
    assertEquals("Lorg/fuwjin/milik/test/ex/ExA;", f.getStack(2).getTypeDesc());
  }

  public void testConstants() throws IncompatibleTypesException {
    MethodFlow flow = getFlow("loop");
    if (flow == null)
      return;
    BasicBlock bb = getBBForMethod(flow, "join");
    Frame f = bb.startFrame;
    assertSame(f.getLocal(2).getConstVal(), Value.NO_VAL);
  }
}
