/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Michael Doberenz -
 * initial implementation
 *******************************************************************************/
package org.fuwjin.pogo;

import java.util.Map;

import org.fuwjin.io.Position;
import org.fuwjin.pogo.parser.Rule;
import org.fuwjin.pogo.reflect.ReflectionType;

/**
 * The standard interface for the Pogo parser/matcher.
 */
public interface Parser {
   Position parse(Position position);

   /**
    * Resolves this parser according to the grammar and type.
    * @param grammar the grammar for resolving any rule references
    * @param type the class for resolving any method references
    */
   void resolve(final String parent, Map<String, Rule> grammar, ReflectionType type);
}
