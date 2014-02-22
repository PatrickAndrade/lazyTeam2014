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
		if (count == null) {
			return;
		}
		
		Time newLap;
		if (laps.isEmpty()) {
			newLap = count.clone();
		} else {
			int totalSecondsNow = count.totalSeconds();
			int totalSecondsLastLap = laps.get(laps.size() - 1).totalSeconds();
			newLap = new Time(totalSecondsNow - totalSecondsLastLap);
		}
		 
		laps.add(newLap);
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
		return (count == null) ? "0 / 0 / 0" : count.getHourMinuteSecond();
	}
}
