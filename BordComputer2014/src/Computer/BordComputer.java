package Computer;

import java.util.concurrent.ArrayBlockingQueue;

import GUI.Window;
import Temps.TimeWorker;

/**
 * This class is the main class of the project since it stores the main methods
 * 
 * @author Gregory Maitre Patrick Andrade
 * 
 */
public class BordComputer implements Runnable {

	enum Update {
		VitesseInstantannee, VitesseMoyenne, KilometrageParcourus, ConsommationInstantanee, ConsommationMoyenne, VolumeEssenceDisponible, AutonomieDisponible, DistancePrevueParRapportAUnObjectif
	}

	private Window mWindow;

	private TimeWorker mTime;

	private double mWheelRadius = 0.38; // Let's suppose that a wheel's radius
										// is approximatively 38cm
	private double mInstantaneousSpeed = 0.0; // Stored as meters/second
	private double mMediumSpeedRAZ = 0.0;
	private double mMediumSpeedFrom0 = 0.0;
	private double mTripDistanceCovered = 0.0;
	private double mDistanceCovered = 0.0;
	private double mInstantaneousConsumption = 0.0; // Stored as liters/meter
	private double mMediumConsumptionRAZ = 0.0;
	private double mMediumConsumptionFrom0 = 0.0;
	private double mEssenceVolume = 0.0;
	private double mAutonomieDisponible = 0.0;
	private double mDistanceToObjective = 0.0;

	private int mSecondsSinceReset = 0;
	private int mSecondsSinceLaunch = 0;

	private int mSleepingTime = 500;

	private ArrayBlockingQueue<Update> mEventUpdateQueue;

	public BordComputer(Window window) {
		mWindow = window;
		mEventUpdateQueue = new ArrayBlockingQueue<Update>(100);
	}

	public void computeInstantaneousSpeed(double hallEffect) {
		// hallEffect given in rad/s
		mInstantaneousSpeed = hallEffect * mWheelRadius;

		mEventUpdateQueue.add(Update.VitesseInstantannee);
		update();
	}

	public void computeInstantaneousConsumption(double injection) { // injection
		// given as
		// cm3/s
		System.out.println("injection : " + injection);
		mInstantaneousConsumption = (mInstantaneousSpeed != 0) ? (injection / 1000.0)
				/ mInstantaneousSpeed
				: 0.0;
		System.out.println("consum : " + mInstantaneousConsumption);

		mEventUpdateQueue.add(Update.ConsommationInstantanee);
		update();
	}

	public void updateMediumSpeedRAZ() {
		// mMediumSpeedRAZ = roundAtTwoDecimals((mMediumSpeedRAZ *
		// mSecondsSinceLaunch + mInstantaneousSpeed * 0.5)
		// / (mSecondsSinceLaunch + 0.5));
		mMediumSpeedRAZ = roundAtTwoDecimals((mMediumSpeedRAZ
				* mSecondsSinceReset + mInstantaneousSpeed)
				/ (mSecondsSinceReset + 1));

		mMediumSpeedFrom0 = roundAtTwoDecimals((mMediumSpeedFrom0
				* mSecondsSinceLaunch + mInstantaneousSpeed)
				/ (mSecondsSinceLaunch + 1));

		mEventUpdateQueue.add(Update.VitesseMoyenne);
		update();
	}

	public void updateDistanceCovered() {
		// mDistanceCovered += mInstantaneousSpeed * mSleepingTime / 1000.0;
		mDistanceCovered += mInstantaneousSpeed;
		mTripDistanceCovered += mInstantaneousSpeed;
		mDistanceToObjective -= mInstantaneousSpeed;

		mEventUpdateQueue.add(Update.KilometrageParcourus);
		mEventUpdateQueue.add(Update.DistancePrevueParRapportAUnObjectif);
		update();
	}

	private void updateMediumConsumption() {
		mMediumConsumptionRAZ = roundAtTwoDecimals((mMediumConsumptionRAZ
				* mSecondsSinceReset + mInstantaneousConsumption)
				/ (mSecondsSinceReset + 1));

		mMediumConsumptionFrom0 = roundAtTwoDecimals((mMediumConsumptionFrom0
				* mSecondsSinceLaunch + mInstantaneousConsumption)
				/ (mSecondsSinceLaunch + 1));

		mEventUpdateQueue.add(Update.ConsommationMoyenne);
		update();
	}

	/**
	 * This method is called each seconds
	 */
	public void updateSeconds() {
		updateDistanceCovered();
		updateMediumSpeedRAZ();
		updateMediumConsumption();
		mSecondsSinceReset++;
		mSecondsSinceLaunch++;
	}

	public void printInstantaneousConsumption() {
		System.out
				.println("Instantanous consumption : "
						+ roundAtTwoDecimals(mInstantaneousConsumption * 1000.0 * 100.0)
						+ " l/100km");
	}

	public void printInstantaneousSpeed() {
		System.out.println("Instantanous speed : "
				+ roundAtTwoDecimals(mInstantaneousSpeed * 3.6) + " km/h");
	}

	public void printMediumSpeed() {
		System.out.println("Medium speed : " + mMediumSpeedRAZ + " m/s");
	}

	public void printDistanceCovered() {
		System.out.println("Distance covered : "
				+ roundAtTwoDecimals(mDistanceCovered) + " meters");
	}

	public void resetTripDistanceCovered() {
		mTripDistanceCovered -= mDistanceCovered;
	}

	public void printTripDistanceCovered() {
		System.out.println("Trip-distance covered : "
				+ (mTripDistanceCovered + mDistanceCovered) + " meters");
	}

	public void setTime(TimeWorker time) {
		mTime = time;
	}

	private double roundAtTwoDecimals(double number) {
		int r = (int) Math.round(number * 100);
		return r / 100.0;
	}

	private synchronized void update() {
		notify();
	}

	private synchronized void waitUntilUpdateWindow() {
		try {
			wait();
		} catch (InterruptedException e) {
		}
	}

	public void run() {
		while (true) {
			while (mEventUpdateQueue.isEmpty()) {
				waitUntilUpdateWindow();
			}

			Update event = mEventUpdateQueue.poll();

			synchronized (this) {
				// TODO:Voir avec Patrick
				switch (event) {
				case AutonomieDisponible:
					mWindow.updateAutonomieDisponible(null);
					break;
				case ConsommationInstantanee:
					mWindow.updateConsommationInstantanee(mInstantaneousConsumption);
					break;
				case ConsommationMoyenne:
					mWindow.updateConsommationMoyenne(mMediumConsumptionRAZ, mMediumConsumptionFrom0);
					break;
				case DistancePrevueParRapportAUnObjectif:
					mWindow.updateDistanceToObective(mDistanceToObjective);
					break;
				case KilometrageParcourus:
					mWindow.updateKilometrageParcourus(mDistanceCovered, mTripDistanceCovered);
					break;
				case VitesseInstantannee:
					mWindow.updateVitesseInstantannee(mInstantaneousSpeed);
					break;
				case VitesseMoyenne:
					mWindow.updateVitesseMoyenne(mMediumSpeedRAZ,
							mMediumSpeedFrom0);
					break;
				case VolumeEssenceDisponible:
					mWindow.updateVolumeEssenceDisponible(null);
					break;
				default:
					break;
				}
			}
		}
	}

	// @Override
	// public void run() {
	// new Thread(mInternalClock).start();
	// double hallEffect = 0.0;
	// double injection = 1;
	// while (true) {
	// computeInstantaneousSpeed(hallEffect);
	// computeInstantaneousConsumption(injection);
	// printInstantaneousSpeed();
	// printInstantaneousConsumption();
	// updateMediumSpeed();
	// updateDistanceCovered();
	// printMediumSpeed();
	// printDistanceCovered();
	// try {
	// Thread.sleep(mSleepingTime);
	// mSecondsSinceLaunch += 0.5;
	// } catch (InterruptedException e) {
	// }
	// hallEffect++;
	// }
	// }
	//
	// public static void main(String[] args) throws InterruptedException {
	// BordComputer b = new BordComputer(new TimeWorker(null), null);
	// new Thread(b).start();
	// }
}
