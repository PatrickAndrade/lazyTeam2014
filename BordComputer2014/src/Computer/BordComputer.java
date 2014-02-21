package Computer;

import Temps.TempsWorker;

/**
 * This class is the main class of the project since it stores the main methods
 * 
 * @author Gregory Maitre Patrick Andrade
 * 
 */
public class BordComputer implements Runnable {
    private TempsWorker mInternalClock = new TempsWorker();

    private double mWheelRadius = 0.38; // Let's suppose that a wheel's radius
                                        // is approximatively 38cm
    private double mInstantaneousSpeed = 0.0; // Stored as meters/second
    private double mInstantaneousConsumption = 0.0; // Stored as liters/meter
    private double mMediumSpeed = 0.0;
    private double mMediumConsumption = 0.0;
    private double mSecondsSinceLaunch = 0.0;
    private double mDistanceCovered = 0.0;
    private double mTripDistanceCovered = 0.0;

    
    private int mSleepingTime = 500;

    public BordComputer(double wheelRadius) {
        mWheelRadius = wheelRadius;
    }

    public void startChronometer() {
        mInternalClock.startChronometre();
    }

    public void stopChronometer() {
        mInternalClock.stopChronometre();
    }

    public void printChronometer() {
        mInternalClock.afficherChronometre();
    }

    public void printClock() {
        System.out.println(mInternalClock);
    }
    
    public void computeInstantaneousConsumption(double injection) { //injection given as cm3/s
        System.out.println("injection : " + injection);
        mInstantaneousConsumption = (mInstantaneousSpeed != 0) ? ( injection / 1000.0 ) / mInstantaneousSpeed : 0.0;
        System.out.println("consum : " + mInstantaneousConsumption);
    }
    
    public void printInstantaneousConsumption() {
        System.out.println("Instantanous consumption : " + roundAtTwoDecimals(mInstantaneousConsumption * 1000.0 * 100.0) + " l/100km");
    }

    public void computeInstantaneousSpeed(double hallEffect) {
        // hallEffect given in rad/s
        mInstantaneousSpeed = hallEffect * mWheelRadius;
    }

    public void printInstantaneousSpeed() {
        System.out.println("Instantanous speed : " + roundAtTwoDecimals(mInstantaneousSpeed * 3.6) + " km/h");
    }

    public void updateMediumSpeed() {
        mMediumSpeed = roundAtTwoDecimals((mMediumSpeed * mSecondsSinceLaunch + mInstantaneousSpeed * 0.5)
                / (mSecondsSinceLaunch + 0.5));
    }

    public void printMediumSpeed() {
        System.out.println("Medium speed : " + mMediumSpeed + " m/s");
    }
    
    public void updateDistanceCovered() {
        mDistanceCovered += mInstantaneousSpeed * mSleepingTime / 1000.0;
    }
    
    public void printDistanceCovered() {
        System.out.println("Distance covered : " + roundAtTwoDecimals(mDistanceCovered) + " meters");
    }
    
    public void resetTripDistanceCovered() {
        mTripDistanceCovered -= mDistanceCovered;
    }
    
    public void printTripDistanceCovered() {
        System.out.println("Trip-distance covered : " + (mTripDistanceCovered + mDistanceCovered) + " meters");
    }

    private double roundAtTwoDecimals(double number) {
        int r = (int) Math.round(number * 100);
        return r / 100.0;
    }

    @Override
    public void run() {
        new Thread(mInternalClock).start();
        double hallEffect = 0.0;
        double injection = 1;
        while (true) {
            computeInstantaneousSpeed(hallEffect);
            computeInstantaneousConsumption(injection);
            printInstantaneousSpeed();
            printInstantaneousConsumption();
            updateMediumSpeed();
            updateDistanceCovered();
            printMediumSpeed();
            printDistanceCovered();
            try {
                Thread.sleep(mSleepingTime);
                mSecondsSinceLaunch += 0.5;
            } catch (InterruptedException e) {
            }
            hallEffect++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BordComputer b = new BordComputer(0.38);
        b.printClock();
        new Thread(b).start();
    }
}
