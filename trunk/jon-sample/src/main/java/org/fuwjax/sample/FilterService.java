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

import java.util.ArrayList;
import java.util.List;

import org.fuwjax.sample.Phone.PhoneType;

/**
 * A more complicated transformation service that filters the set
 * of models and returns them in an arraylist.
 */
public class FilterService implements TransformationService{
	private Filter filter;
	/**
	 * Creates a new instance.
	 * @param filter the filter for the transform
	 */
	public FilterService(Filter filter){
		this.filter = filter;
	}
	
	public Object transform(List<Model> models){
		List<Model> output = new ArrayList<Model>();
		for(Model model: models){
			if(filter.pass(model)){
				output.add(model);
			}
		}
		return output;
	}
	
	/**
	 * An interface for the FilterService to filter the set of models.
	 */
	public interface Filter{
		/**
		 * Returns true if <code>model</code> should be included in the filtered set.
		 * @param model the model currently under test
		 * @return true if <code>model</code> should be included in the filtered set, false otherwise
		 */
		boolean pass(Model model);
	}
	
	/**
	 * Passes a model or filter without any test.
	 */
	public static class PassFilter implements Filter, PhoneFilter{
		public boolean pass(Model model){
		   return true;
		}
		
		public boolean pass(Phone phone){
		   return true;
		}
	}
	
	/**
	 * Requires that all of a set of filters pass a model.
	 */
	public static class AndFilter implements Filter{
		private final Filter[] filters;
		/**
		 * Creates a new instance.
		 * @param filters the set of filters which must all pass a model
		 */
		public AndFilter(Filter... filters){
			this.filters = filters;
		}
		
		public boolean pass(Model model){
			for(Filter filter: filters){
				if(!filter.pass(model)){
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * Requires that at least one of a set of filters pass a model.
	 */
	public static class OrFilter implements Filter{
		private final Filter[] filters;
		/**
		 * Creates a new instance.
		 * @param filters the set of filters, one of which must pass a model
		 */
		public OrFilter(Filter... filters){
			this.filters = filters;
		}
		
		public boolean pass(Model model){
			for(Filter filter: filters){
				if(filter.pass(model)){
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Negates a filter.
	 */
	public static class NotFilter implements Filter{
		private final Filter filter;
		/**
		 * Creates a new instance.
		 * @param filter the filter which must not pass a model
		 */
		public NotFilter(Filter filter){
			this.filter = filter;
		}
		
		public boolean pass(Model model){
		   return !filter.pass(model);
		}
	}
	
	/**
	 * Filters based on the first part of a name.
	 */
	public static class NameFilter implements Filter{
		private String nameStart;
		/**
		 * Crates a new instance.
		 * @param nameStart the start of a name that will pass
		 */
		public NameFilter(String nameStart){
			this.nameStart = nameStart;
		}
		
		public boolean pass(Model model){
		   return model.getName().startsWith(nameStart);
		}
	}
	
	/**
	 * Filters based on the type of contact.
	 */
	public static class PhoneTypeFilter implements Filter{
		private final PhoneFilter filter;
		private final PhoneType type;
		/**
		 * Applies <code>filter</code> to the contact of type <code>type</code>.
		 * @param filter the filter to apply
		 * @param type the type of contatct to filter on
		 */
		public PhoneTypeFilter(PhoneFilter filter, PhoneType type){
			this.filter = filter;
			this.type = type;
		}
		
		public boolean pass(Model model){
			Phone phone = model.getContact(type);
			return filter.pass(phone);
		}
	}
	
	/**
	 * A filter that only checks the phone field.
	 */
	public interface PhoneFilter{
		/**
		 * Returns true if the model containing <code>phone</code> should be passed.
		 * @param phone the contact phone to filter on
		 * @return true if the model should be passed, false otherwise
		 */
		boolean pass(Phone phone);
	}
	
	/**
	 * Filters based on the area code.
	 */
	public static class AreaCodeFilter implements PhoneFilter{
		private int areaCode;
		/**
		 * Creates a new filter.
		 * @param areaCode the area code which will pass
		 */
		public AreaCodeFilter(int areaCode){
			this.areaCode = areaCode;
		}
		
		public boolean pass(Phone phone){
			return phone.getAreaCode() == areaCode;
		}
	}
}
