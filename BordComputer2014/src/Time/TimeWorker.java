package Time;

import Computer.BordComputer;
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
	private BordComputer bordComputer;

	public TimeWorker(BordComputer bordComputer, Window window) {
		now = new Time();
		now.now();
		timeRuns = new Time();
		timeRuns.reset();
		chronometer = new Chronometer();
		this.window = window;
		this.bordComputer = bordComputer;
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

	public boolean lapChronometer() {
		return chronometer.lap();
	}

	public void stopChronometer() {
		chronometer.stop();
	}

	private void update() {
		now.update();
		timeRuns.update();
		chronometer.update();
		bordComputer.updateSeconds();
		

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
