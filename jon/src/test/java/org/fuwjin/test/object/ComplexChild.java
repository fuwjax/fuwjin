/*
 * This file is part of JON.
 *
 * JON is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * JON is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2007 Michael Doberenz
 */
package org.fuwjin.test.object;

/**
 * A test case that builds a replacement for one of its parent's fields
 * @author michaeldoberenz
 */
public class ComplexChild extends SimpleObject {
   /**
    * Creates a new instance.
    */
   public ComplexChild() {
      super("wow", new IntegerChild(new String[]{"bob", "was", "here"}, 19)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
   }
}
