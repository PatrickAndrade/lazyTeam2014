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
    private double mInstantaneousSpeed = 0.0; // Stored as m/s
    private double mMediumSpeed = 0.0;
    private double mSecondsSinceLaunch = 0.0;

    private int mDistanceCovered = 0;

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

    public void computeInstantaneousSpeed(double hallEffect) {
        // hallEffect given in rad/s
        mInstantaneousSpeed = hallEffect * mWheelRadius;
    }

    public void printInstantaneousSpeed() {
        System.out.println("Instantanous speed : " + roundAtTwoDecimals(mInstantaneousSpeed) + " m/s");
    }

    public void uptadeMediumSpeed() {
        mMediumSpeed = roundAtTwoDecimals((mMediumSpeed * mSecondsSinceLaunch + mInstantaneousSpeed * 0.5)
                / (mSecondsSinceLaunch + 0.5));
    }

    public void printMediumSpeed() {
        System.out.println("Medium speed : " + mMediumSpeed + " m/s");
    }

    private double roundAtTwoDecimals(double number) {
        int r = (int) Math.round(number * 100);
        return r / 100.0;
    }

    @Override
    public void run() {
        new Thread(mInternalClock).start();
        double hallEffect = 0.0;
        while (true) {
            computeInstantaneousSpeed(hallEffect);
            printInstantaneousSpeed();
            uptadeMediumSpeed();
            printMediumSpeed();
            try {
                Thread.sleep(500);
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
