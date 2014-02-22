package Temps;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class TempsWorker implements Runnable {

	private Time now;
	private Time timeRuns;
	private Chronometer chronometer;

	public TempsWorker() {
		now = new Time();
		now.now();
		timeRuns = new Time();
		timeRuns.reset();
		chronometer = new Chronometer();
	}

	private void waitOneSeconde() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	public void reset() {
		timeRuns.reset();
	}

	public void showTimeRuns() {
		System.out.println("Temps de parcourt : "
				+ timeRuns.getHourMinuteSecond());
	}
	
	public Time getTimeRuns() {
	    return timeRuns;
	}

	public void startChronometrer() {
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

	public void showChronometer() {
		System.out
				.println(chronometer);
	}
	
	public String toString() {
	    return now.toString();
	}

	private void update() {
		now.update();
		timeRuns.update();
		chronometer.update();
	}

	@Override
	public void run() {
		while (true) {
			waitOneSeconde();
			update();
			System.out.println("Date : " + now);
			showTimeRuns();
			showChronometer();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		TempsWorker t = new TempsWorker();
		new Thread(t).start();
		
		Thread.sleep(5000);
		t.startChronometrer();
		Thread.sleep(7000);
		t.stopChronometer();
		t.reset();
	}
}
