package org.fuwjin.gravitas.gesture.command;

import org.fuwjin.gravitas.gesture.GestureRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;

public class ClearEventsCommand implements Runnable {
	@Inject
	private GestureRouter router;
	@Inject
	private Integration source;
	
	@Override
	public void run() {
       int removed = router.clear();
       if(removed == 0){
          source.notify("The queue is empty");
       }else if(removed == 1){
          source.notify("Removed 1 event");
       }else{
          source.notify(String.format("Removed %d events",removed));
       }
	}
}
