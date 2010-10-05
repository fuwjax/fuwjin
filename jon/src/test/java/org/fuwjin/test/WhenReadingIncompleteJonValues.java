/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.test;

import static org.junit.Assert.assertNull;

import java.util.Map;

import org.fuwjin.jon.JonReader;
import org.fuwjin.pogo.PogoException;
import org.junit.Test;

/**
 * Demos JON reading incomplete values.
 */
public class WhenReadingIncompleteJonValues {
   /**
    * A forward reference must be present.
    * @throws PogoException if it fails
    */
   @Test
   public void shouldNotPutForwardReferenceInMap() throws PogoException {
      final Map<?, ?> result = new JonReader("{\"a\":&1}").read(Map.class);
      assertNull(result.get("a"));
   }
}
