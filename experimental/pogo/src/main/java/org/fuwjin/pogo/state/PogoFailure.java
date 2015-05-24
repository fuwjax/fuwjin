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
package org.fuwjin.pogo.state;

import java.util.LinkedList;
import java.util.List;

import org.fuwjin.pogo.PogoException;
import org.fuwjin.postage.Failure;
import org.fuwjin.util.CodePointSet;

/**
 * Represents the most recent failure during a pogo operation.
 */
public class PogoFailure {
   private static class FailureTrace {
      private final String name;
      private final AbstractPosition position;
      private final int level;

      private FailureTrace(final AbstractPosition position, final String name, final int level) {
         this.level = level;
         this.name = name;
         this.position = position;
      }

      @Override
      public String toString() {
         return "  in " + name + position;
      }
   }

   private final CodePointSet set = new CodePointSet();
   private AbstractPosition current;
   private final List<FailureTrace> stack = new LinkedList<FailureTrace>();
   private String message;
   private Failure cause;

   /**
    * Creates an exception corresponding to this failure.
    * @return the new exception
    */
   public PogoException exception() {
      if(message == null) {
         return new PogoException(current, "failed test: " + ((ParsePosition)current).toChar() + " expecting [" + set
               + "]", stack);
      }
      if(cause == null) {
         return new PogoException(current, message, stack);
      }
      return new PogoException(current, message, cause.exception(), stack);
   }

   /**
    * Records a failure to match a character to a range. The test character
    * belongs to the position.
    * @param position the position and test character
    * @param start the start of the expected range
    * @param end the end of the expected range
    */
   public void fail(final AbstractPosition position, final int start, final int end) {
      if(current == null) {
         current = position;
      } else if(position.index() > current.index()) {
         fail(position, null, null);
      } else if(current.index() > position.index()) {
         return;
      }
      set.unionRange(start, end);
   }

   /**
    * Records a failure during postage reflection.
    * @param position the position of the failure
    * @param newMessage the description of the failure
    * @param newCause the cause of the failure
    */
   public void fail(final AbstractPosition position, final String newMessage, final Failure newCause) {
      if(current == null || position.index() >= current.index()) {
         current = position;
         set.clear();
         stack.clear();
         message = newMessage;
         cause = newCause;
      }
   }

   /**
    * Records a rule involved in the failure.
    * @param position the failure position
    * @param name the name of the rule
    * @param level the stack level of the rule
    */
   public void failStack(final AbstractPosition position, final String name, final int level) {
      if(stack.size() == 0 || stack.get(stack.size() - 1).level > level) {
         stack.add(new FailureTrace(position, name, level));
      }
   }
}
