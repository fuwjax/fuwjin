package org.fuwjin.gravitas.parser;

import static java.util.Collections.unmodifiableCollection;

import java.util.LinkedList;
import java.util.List;

public class Instruction {
	private String type;
	private Class<?> cls;
	private List<Atom> atoms = new LinkedList<Atom>();
	
	void addAtom(Atom atom){
		atoms.add(atom);
	}

	public Runnable newInstance(String... elements) throws InstantiationException, IllegalAccessException {
		if(atoms.size() != elements.length){
			return null;
		}
		int index = 0;
		Runnable runner = (Runnable)cls.newInstance();
		for(Atom atom: atoms){
			if(!atom.apply(runner, elements[index++])){
				return null;
			}
		}
		return runner;
	}
	
	public Class<?> type(){
	   return cls;
	}

	void resolve(ClassResolver resolver) {
		cls = resolver.forName(type);
		assert Runnable.class.isAssignableFrom(cls);
		for(Atom atom: atoms){
			atom.resolve(cls);
		}
	}
	
	public Iterable<Atom> atoms(){
	   return unmodifiableCollection(atoms);
	}

   public String description(){
      try{
         return cls.newInstance().toString();
      }catch(Exception e){
         return "ERROR: No Description Found";
      }
   }
}
