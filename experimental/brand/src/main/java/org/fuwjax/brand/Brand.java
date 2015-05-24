package org.fuwjax.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Brand {
	private final Random r = new Random();
	private final Frame root;
	private double[] p;

	public Brand(double[] p) {
		this.p = p;
		int count = 0;
		double average = 0;
		for(double pi: p){
			if(pi > 0){
				average += (pi - average)/++count;
			}
		}
		root = new Frame(0, average);
	}

	public int nextInteger() {
		return root.nextInteger();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("coverage    min   ->  max   : indicies");
		Frame frame = root;
		while(frame != null){
			builder.append('\n').append(frame);
			frame = frame.next;
		}
		return builder.toString();
	}

	public class Frame {
		private int[] indicies;
		private double delta;
		private Frame next;
		private double min;
		
		public Frame(double min, double max) {
			this.min = min;
			this.delta = max - min;
			double average = 0; // average of the probabilities greater than max
			int count = 0; // count of the probabilities greater than max
			List<Integer> arr = new ArrayList<Integer>();
			for (int i = 0; i < p.length; i++) {
				if (p[i] > min) {
					arr.add(i);
					if (p[i] > max) {
						average += (p[i]-average)/++count;
					}
				}
			}
			indicies = new int[arr.size()];
			for (int i = 0; i < indicies.length; i++) {
				indicies[i] = arr.get(i);
			}
			if(count > 0){
				next = new Frame(max, average);
			}
		}
		
		public int nextInteger(){
			double testP = min + r.nextDouble() * delta;
			int index = indicies[(int) (r.nextDouble() * indicies.length)];
			if (testP < p[index]) {
				return index;
			}
			return next.nextInteger();
		}
		
		private double coverage(){
			if(next == null){
				return delta*indicies.length;
			}
			return (delta * indicies.length) - (next.delta*next.indicies.length);
		}
		
		@Override
		public String toString() {
			return String.format("%.6f  %.6f->%.6f: %d",coverage(),min,(min+delta),indicies.length);
		}
	}
}
