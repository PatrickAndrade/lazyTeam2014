package Temps;

import GUI.Window;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class TimeWorker implements Runnable {

	private Time now;
	private Time timeRuns;
	private Chronometer chronometer;
	private Window window;

	public TimeWorker(Window window) {
		now = new Time();
		now.now();
		timeRuns = new Time();
		timeRuns.reset();
		chronometer = new Chronometer();
		this.window = window;
	}

	private void waitOneSeconde() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	public void resetTimeRuns() {
		timeRuns.reset();
		window.updateTime();
	}

	public String getTimeRuns() {
		return timeRuns.getHourMinuteSecond();
	}

	public String getChronometer() {
		return chronometer.toString();
	}

	public String getTime() {
		return now.toString();
	}

	public String getLastLap() {
		return chronometer.lastLap();
	}

	public void startChronometer() {
		chronometer.start();
	}

	public void pauseChronometer() {
		chronometer.pause();
	}

	public void lapChronometer() {
		chronometer.lap();
	}

	public void stopChronometer() {
		chronometer.stop();
	}

	private void update() {
		now.update();
		timeRuns.update();
		chronometer.update();

		if (window != null) {
			window.updateTime();
		}
	}

	@Override
	public void run() {
		while (true) {
			waitOneSeconde();
			update();
		}
	}
}
