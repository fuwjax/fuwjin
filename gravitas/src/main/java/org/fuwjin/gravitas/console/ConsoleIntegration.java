package org.fuwjin.gravitas.console;

import static java.lang.System.in;
import static java.lang.System.out;
import static java.lang.Thread.interrupted;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.fuwjin.gravitas.gesture.GestureRouter;
import org.fuwjin.gravitas.gesture.Integration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ConsoleIntegration implements Integration {
	@Inject
	private GestureRouter router;
	private BufferedReader reader = new BufferedReader(
			new InputStreamReader(in));
	private Runnable runner = new Runnable() {
		public void run() {
			readFromIn();
		}
	};
	public ConsoleIntegration(){
		Thread readThread = new Thread(runner);
		readThread.setDaemon(true);
		readThread.start();
	}

	@Override
	public void notify(Object... messages) {
		for (Object message : messages) {
			out.println(message);
		}
	}

	protected void readFromIn() {
		try{
			while(!interrupted()){
				String line = reader.readLine();
				if(line != null){
					router.raise(this, line);
				}
			}
		}catch(IOException e){
			// continue
		}
	}
}
