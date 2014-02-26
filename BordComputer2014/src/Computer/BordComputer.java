package Computer;

import java.util.concurrent.ConcurrentLinkedQueue;

import GUI.Window;

/**
 * This class is the main class of the project since it stores the main methods
 * 
 * @author Gregory Maitre Patrick Andrade
 * 
 */
public class BordComputer implements Runnable {

	enum Update {
		VitesseInstantannee, VitesseMoyenne, KilometrageParcourus, ConsommationInstantanee, ConsommationMoyenne, VolumeEssenceDisponible, AutonomieDisponible, DistancePrevueParRapportAUnObjectif, Position
	}

	private Window mWindow;

	private double mWheelRadius = 0.38; // Let's suppose that a wheel's radius
										// is approximatively 38cm
	private double mInstantaneousSpeed = 0.0; // Stored as meters/second
	private double mMediumSpeedRAZ = 0.0; // Stored as meters/second
	private double mMediumSpeedFrom0 = 0.0; // Stored as meters/second
	private double mTripDistanceCovered = 0.0; // Stored as meter
	private double mDistanceCovered = 0.0;  // Stored as meter
	private double mInstantaneousConsumption = 0.0; // Stored as liters/s
	private double mMediumConsumptionRAZ = 0.0; // Stored as liters/s
	private double mMediumConsumptionFrom0 = 0.0; // Stored as liters/s
	private double mEssenceVolumeDisponible = 0.0; // Stored as liter
	private double mAutonomieDisponible = 0.0; // Stored in meter
	private double mDistanceToObjective = 100.0 * 1000.0; //Stored as meter
	private double mLatitude = 0.0;
	private double mLongitude = 0.0;

	private int mSecondsSinceReset = 0;
	private int mSecondsSinceLaunch = 0;

	private ConcurrentLinkedQueue<Update> mEventUpdateQueue;

	public BordComputer(Window window) {
		mWindow = window;
		mEventUpdateQueue = new ConcurrentLinkedQueue<Update>();
	}

	public synchronized void computeInstantaneousSpeed(double hallEffect) {
		// hallEffect given in rad/s
		mInstantaneousSpeed = hallEffect * mWheelRadius;

		mEventUpdateQueue.add(Update.VitesseInstantannee);
		update();
	}

	public synchronized void computeInstantaneousConsumption(double injection) { // injection
		// given as
		// cm3/s
		if (mEssenceVolumeDisponible < injection) {
			mInstantaneousConsumption = 0;
		} else {

			mInstantaneousConsumption = (mInstantaneousSpeed != 0) ? (injection / 1000.0)
					/ mInstantaneousSpeed
					: 0.0;
			updateAutonomieDisponible();
		}

		mEventUpdateQueue.add(Update.ConsommationInstantanee);
		update();
	}

	public synchronized void computeEssenceVolumeDisponible(double volume) { //en litre
		mEssenceVolumeDisponible = volume;
		updateAutonomieDisponible();

		mEventUpdateQueue.add(Update.VolumeEssenceDisponible);
		update();
	}
	
	public synchronized void computePosition(double value1, double value2) {
		mLatitude = value1;
		mLongitude = value2;
		mEventUpdateQueue.add(Update.Position);
		update();
	}
	
	private synchronized void updateAutonomieDisponible() { // en metre
		if (mMediumConsumptionFrom0 != 0) {
			mAutonomieDisponible = (mEssenceVolumeDisponible / mMediumConsumptionFrom0) * mMediumSpeedFrom0;
		} //(liter / (liter/s)) * m/s = m
		
		mEventUpdateQueue.add(Update.AutonomieDisponible);
		update();
	}

	public synchronized void updateMediumSpeed() {
		mMediumSpeedRAZ = roundAtTwoDecimals((mMediumSpeedRAZ
				* mSecondsSinceReset + mInstantaneousSpeed)
				/ (mSecondsSinceReset + 1));

		mMediumSpeedFrom0 = roundAtTwoDecimals((mMediumSpeedFrom0
				* mSecondsSinceLaunch + mInstantaneousSpeed)
				/ (mSecondsSinceLaunch + 1));
		
		mEventUpdateQueue.add(Update.VitesseMoyenne);
		update();
	}

	public synchronized void updateDistanceCovered() {
		mDistanceCovered += mInstantaneousSpeed;
		mTripDistanceCovered += mInstantaneousSpeed;
		
		if (mDistanceToObjective > 0) {
			mDistanceToObjective -= mInstantaneousSpeed;
		}

		mEventUpdateQueue.add(Update.KilometrageParcourus);
		mEventUpdateQueue.add(Update.DistancePrevueParRapportAUnObjectif);
		update();
	}

	private synchronized void updateMediumConsumption() {
		mMediumConsumptionRAZ = (mMediumConsumptionRAZ
				* mSecondsSinceReset + mInstantaneousConsumption)
				/ (mSecondsSinceReset + 1);

		mMediumConsumptionFrom0 = (mMediumConsumptionFrom0
				* mSecondsSinceLaunch + mInstantaneousConsumption)
				/ (mSecondsSinceLaunch + 1);

		mEventUpdateQueue.add(Update.ConsommationMoyenne);
		update();
	}

	/**
	 * This method is called each seconds
	 */
	public void updateSeconds() {
		updateDistanceCovered();
		updateMediumSpeed();
		updateMediumConsumption();
		updateAutonomieDisponible();
		mSecondsSinceReset++;
		mSecondsSinceLaunch++;
	}

	public synchronized void reset() {
		mTripDistanceCovered = 0.0;
		mSecondsSinceReset = 0;
		mMediumConsumptionRAZ = 0.0;
		mMediumSpeedRAZ = 0.0;
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
					mWindow.updateAutonomieDisponible(mAutonomieDisponible);
					break;
				case ConsommationInstantanee:
					mWindow.updateConsommationInstantanee(mInstantaneousConsumption);
					break;
				case ConsommationMoyenne:
					mWindow.updateConsommationMoyenne(mMediumConsumptionRAZ,
							mMediumConsumptionFrom0);
					break;
				case DistancePrevueParRapportAUnObjectif:
					mWindow.updateDistanceToObective(mDistanceToObjective);
					break;
				case KilometrageParcourus:
					mWindow.updateKilometrageParcourus(mDistanceCovered,
							mTripDistanceCovered, mSecondsSinceLaunch);
					break;
				case VitesseInstantannee:
					mWindow.updateVitesseInstantannee(mInstantaneousSpeed);
					break;
				case VitesseMoyenne:
					mWindow.updateVitesseMoyenne(mMediumSpeedRAZ,
							mMediumSpeedFrom0);
					break;
				case VolumeEssenceDisponible:
					mWindow.updateVolumeEssenceDisponible(mEssenceVolumeDisponible, mSecondsSinceLaunch);
					break;
				case Position:
			        mWindow.positionMap(mLatitude, mLongitude);
			        break;
				default:
					break;
				}
			}
		}
	}
}
