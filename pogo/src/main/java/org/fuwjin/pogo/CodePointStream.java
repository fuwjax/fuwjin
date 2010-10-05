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
package org.fuwjin.pogo;

/**
 * Allows code points or bytes to be streamed one at a time.
 */
public interface CodePointStream {
   /**
    * The End of File marker. Once a stream returns EOF, it will only return
    * EOF.
    */
   int EOF = -1;

   /**
    * Returns the next code point or byte, or EOF if the end of the stream has
    * been reached.
    * @return the next code point.
    */
   int next();
}
