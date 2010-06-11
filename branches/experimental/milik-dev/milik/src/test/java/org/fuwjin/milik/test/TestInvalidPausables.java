/* Copyright (c) 2006, Sriram Srinivasan
 *
 * You may distribute this software under the terms of the license 
 * specified in the file "License"
 */

package org.fuwjin.milik.test;

import junit.framework.TestCase;

import org.fuwjin.milik.KilimException;
import org.fuwjin.milik.analysis.Detector;
import org.fuwjin.milik.tools.Weaver;

public class TestInvalidPausables extends TestCase {
  private void ensureException(String className) {
    try {
      Weaver.weaveClass2(className, Detector.DEFAULT);
      fail("Expected weave exception while processing " + className);
    } catch (KilimException ke) {
    } catch (Exception e) {
      fail(e.toString());
    }
  }

  public void testWeaveConstructor() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidConstructor");
  }

  public void testWeaveSynchronized() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidSynchronized");
    ensureException("org.fuwjin.milik.test.ex.ExInvalidSynchronized1");
  }

  public void testWeaveStatic() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidStaticBlock");
  }

  public void testWeaveMethod() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidCallP_NP");
  }

  public void testWeaveSuperPausable() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidNPDerived");

  }

  public void testWeaveSuperNotPausable() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidPDerived");
  }

  public void testWeaveInterfacePausable() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidPImp");

  }

  public void testWeaveInterfaceNotPausable() {
    ensureException("org.fuwjin.milik.test.ex.ExInvalidNPImp");

  }
}
