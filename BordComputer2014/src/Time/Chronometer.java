package Time;

import java.util.ArrayList;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Chronometer {
	private Time count;
	private Time lastLapTime;
	private ArrayList<Time> laps;
	private boolean isChronometerCount;
	
	public Chronometer() {
		laps = new ArrayList<Time>();
		isChronometerCount = false;
	}
	
	public synchronized void start() {
		if (count == null) {
			count = new Time();
			lastLapTime = null;
			laps.clear();
		}
		isChronometerCount = true;
	}
	
	public synchronized void pause() {
		isChronometerCount = false;
	}
	
	public synchronized boolean lap() {
		if (count == null) {
			return false;
		}
		
		Time newLap;
		if (lastLapTime == null) {
			newLap = count.clone();
		} else {
			int totalSecondsNow = count.totalSeconds();
			int totalSecondsLastLap = lastLapTime.totalSeconds();
			newLap = new Time(totalSecondsNow - totalSecondsLastLap);
		}
		
		laps.add(newLap);
		lastLapTime = count.clone();
		return true;
	}
	
	public synchronized void stop() {
		pause();
		lap();
		count = null;
	}
	
	public synchronized String lastLap() {
		int size = laps.size();
		if (size == 0) {
			return "";
		}
		return laps.get(size - 1).getHourMinuteSecond();
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
