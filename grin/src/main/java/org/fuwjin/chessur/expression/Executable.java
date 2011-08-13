/*******************************************************************************
 * Copyright (c) 2011 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Doberenz - initial API and implementation
 ******************************************************************************/
package org.fuwjin.chessur.expression;

import javax.script.ScriptException;
import org.fuwjin.chessur.Script;
import org.fuwjin.dinah.adapter.StandardAdapter;
import org.fuwjin.grin.env.Trace;

/**
 * Base utility class for all things transformative.
 */
public abstract class Executable implements Script {
   @Override
   public Object eval(final Trace trace) throws ScriptException {
      try {
         final Object result = expression().resolve(trace);
         if(StandardAdapter.isSet(result)) {
            return result;
         }
         return null;
      } catch(final GrinException e) {
         final ScriptException ex = new ScriptException(String.format("Execution of %s aborted", name()));
         ex.initCause(e);
         throw ex;
      }
   }

   protected abstract Expression expression();
}
