/*******************************************************************************
 * Copyright (c) 2010 Michael Doberenz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Doberenz - initial implementation
 *******************************************************************************/
package org.fuwjax.sample;

import java.util.List;

/**
 * A hypothetical business service.
 */
public interface TransformationService{
	/**
	 * The service method.
	 * @param models the set of models which should be transformed
	 * @return the transformation
	 */
	Object transform(List<Model> models);
}
