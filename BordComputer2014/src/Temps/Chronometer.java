package Temps;

import java.util.ArrayList;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Chronometer {
	private Time count;
	private ArrayList<Time> laps;
	private boolean isChronometerCount;
	
	public Chronometer() {
		laps = new ArrayList<Time>();
		isChronometerCount = false;
	}
	
	public synchronized void start() {
		if (count == null) {
			count = new Time();
			laps.clear();
		}
		isChronometerCount = true;
	}
	
	public synchronized void pause() {
		isChronometerCount = false;
	}
	
	public synchronized void lap() {
		laps.add(count);
	}
	
	public synchronized void stop() {
		pause();
		lap();
		count = null;
	}
	
	public synchronized void update() {
		if (isChronometerCount) {
			count.update();
		}
	}
	
	public synchronized String toString() {
		return count.toString();
	}
}
