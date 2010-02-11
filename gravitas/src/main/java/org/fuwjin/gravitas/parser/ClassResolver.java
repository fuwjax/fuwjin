package org.fuwjin.gravitas.parser;

import java.util.LinkedList;
import java.util.List;

class ClassResolver {
	private List<String> packages = new LinkedList<String>();

	void addPackage(String packageName){
		packages.add(packageName);
	}
	
	Class<?> forName(String type) {
		for(String pkg: packages){
			try {
				return Class.forName(pkg+'.'+type);
			} catch (ClassNotFoundException e) {
				// continue;
			}
		}
		throw new RuntimeException("Could not locate "+type+". Add a \"use\" statement to context.gravitas.");
	}
}
