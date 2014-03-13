package Sensor;

import Computer.BordComputer;

/**
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Sensors implements Runnable {

    private BordComputer bordComputer;
    private double wheelRadius = 0.38; // Let's suppose that a wheel's radius
                                       // is approximatively 38cm

    public Sensors(BordComputer bordComputer) {
        this.bordComputer = bordComputer;
    }

    private void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    public void run() {

        double injection = 2.0;
        double volume = 45.0;
        int latIndex = 0;
        int longIndex = 0;
        double speed = 0.0;

        while (true) {

            // position:
            if ((latIndex >= Latitude.values.length) || (longIndex >= Longitude.values.length)) {
                latIndex = 0;
                longIndex = 0;
            }

            if ((latIndex + 3 < Latitude.values.length) || (longIndex + 3 < Longitude.values.length)) {
                speed = latitudeLongitudeToDistance(Latitude.values[latIndex], Longitude.values[longIndex], Latitude.values[latIndex + 3],
                        Longitude.values[longIndex + 3])/3.0;
            }

            bordComputer.computePosition(Latitude.values[latIndex], Longitude.values[longIndex]);

            latIndex++;
            longIndex++;

            // Hall effect
            bordComputer.computeInstantaneousSpeed(speed / wheelRadius);

            // injection
            bordComputer.computeInstantaneousConsumption(injection);

            // volume:
            bordComputer.computeEssenceVolumeDisponible(volume);

            if (volume - injection >= 0) {
                volume -= injection / 1000;
            }

            waitOneSecond();
        }
    }

    private double latitudeLongitudeToDistance(double lastLatitude, double lastLongitude, double latitude,
            double longitude) {
        double R = 6371 * 1000; // rayon de la Terre

        double dLat = Math.toRadians(latitude - lastLatitude);
        double dLon = Math.toRadians(longitude - lastLongitude);
        double lat1 = Math.toRadians(lastLatitude);
        double lat2 = Math.toRadians(latitude);

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1)
                * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    

    

}