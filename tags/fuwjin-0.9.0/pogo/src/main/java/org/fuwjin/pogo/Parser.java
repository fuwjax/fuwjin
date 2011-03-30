/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

/**
 * The standard interface for the Pogo parser/matcher.
 */
public interface Parser {
   /**
    * Parses starting at the given position. If this rule matches, it will
    * return the final position. If it does not match, it returns the given
    * position.
    * @param position the start position
    * @return the final position
    */
   Position parse(Position position);

   /**
    * Resolves this parser according to the grammar and type.
    * @param grammar the grammar for resolving any rule references
    * @param parent the containing rule
    */
   void resolve(Grammar grammar, Rule parent);
}