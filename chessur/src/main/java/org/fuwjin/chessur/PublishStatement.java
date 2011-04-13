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
package org.fuwjin.chessur;

/**
 * Publishes a value to an outstream.
 */
public class PublishStatement implements Expression {
   private final Expression value;

   /**
    * Creates a new instance.
    * @param value the value to publish
    */
   public PublishStatement(final Expression value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return "publish " + value;
   }

   @Override
   public State transform(final State state) {
      final State result = value.transform(state);
      if(!result.isSuccess()) {
         return result;
      }
      return result.publish();
   }
}
