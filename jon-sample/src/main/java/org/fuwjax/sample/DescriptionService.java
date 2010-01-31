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
