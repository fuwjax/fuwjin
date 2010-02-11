package org.fuwjin.gravitas.parser;

import java.util.LinkedList;
import java.util.List;

public class Context {
	private String type;
	private Class<?> cls;
	private List<Command> commands = new LinkedList<Command>();
	void addCommand(Command command){
		commands.add(command);
	}
	
	Class<?> type() {
		return cls;
	}
	
	public Runnable parse(String input) throws InstantiationException, IllegalAccessException{
		String[] split = input.split(" ");
		for(Command command: commands){
			Runnable task = command.newInstance(split);
			if(task != null){
				return task;
			}
		}
		return null;
	}
	void resolve(ClassResolver resolver) {
		cls = resolver.forName(type);
		for(Command command: commands){
			command.resolve(resolver);
		}
	}
}
