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
package org.fuwjax.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple transformation service that simply pulls the names and descriptions
 * from each of the models and returns them as a map of names to descriptions.
 */
public class DescriptionService implements TransformationService{
	public Object transform(List<Model> models){
		Map<String, String> descs = new HashMap<String, String>();
		for(Model model: models){
			descs.put(model.getName(), model.getDescription());
		}
	   return descs;
	}
}
