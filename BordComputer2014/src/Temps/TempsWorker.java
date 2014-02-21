package Temps;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class TempsWorker implements Runnable {

	private Time maintenant;
	private Time tempsDeParcourt;
	private Time chronometre;
	private boolean isChronometreCount = false;

	public TempsWorker() {
		maintenant = new Time();
		maintenant.now();
		tempsDeParcourt = new Time();
		tempsDeParcourt.zero();
		chronometre = new Time();
		//chronometre.zero();
	}

	private void waitOneSeconde() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}

	public void reset() {
		tempsDeParcourt.zero();
	}

	public synchronized void afficherTempsDeParcourt() {
		System.out.println("Temps de parcourt : "
				+ tempsDeParcourt.heureMinutesSeconde());
	}
	
	public synchronized Time getTempsDeParcourt() {
	    return tempsDeParcourt;
	}

	public synchronized void startChronometre() {
		chronometre.zero();
		isChronometreCount = true;
	}

	public synchronized void stopChronometre() {
		isChronometreCount = false;
	}

	public synchronized void afficherChronometre() {
		System.out
				.println("Chronometer : " + chronometre.heureMinutesSeconde());
	}
	
	public String toString() {
	    return maintenant.toString();
	}

	private synchronized void update() {
		maintenant.update();
		tempsDeParcourt.update();

		if (isChronometreCount) {
			chronometre.update();
		}
	}

	@Override
	public void run() {
		while (true) {
			waitOneSeconde();
			update();
			//System.out.println("Date : " + maintenant);
			//afficherTempsDeParcourt();
			//afficherChronometre();
		}
	}

//	public static void main(String[] args) throws InterruptedException {
//		TempsWorker t = new TempsWorker();
//		new Thread(t).start();
//		
//		Thread.sleep(5000);
//		t.startChronometre();
//		Thread.sleep(7000);
//		t.stopChronometre();
//		t.reset();
//		
//	}
}
