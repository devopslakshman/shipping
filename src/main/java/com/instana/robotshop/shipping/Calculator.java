package com.instana.robotshop.shipping;

public class Calculator {

    private double latitude = 0;
    private double longitude = 0;

    public Calculator(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Calculator(City city) {
        this.latitude = city.getLatitude();
        this.longitude = city.getLongitude();
    }

    /**
     * Calculate the distance between this location and the target location.
     * Use decimal latitude/longitude degrees.
     * Formula is Haversine.
     */
    public long getDistance(double targetLatitude, double targetLongitude) {

        double earthRadius = 6371e3; // meters

        // Convert to radians
        double latitudeR = Math.toRadians(this.latitude);
        double targetLatitudeR = Math.toRadians(targetLatitude);

        // Difference in radians
        double diffLatR = Math.toRadians(targetLatitude - this.latitude);
        double diffLongR = Math.toRadians(targetLongitude - this.longitude);

        double a = Math.sin(diffLatR / 2.0) * Math.sin(diffLatR / 2.0)
                + Math.cos(latitudeR) * Math.cos(targetLatitudeR)
                * Math.sin(diffLongR / 2.0) * Math.sin(diffLongR / 2.0);

        double c = 2.0 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1.0 - a)
        );

        return (long) Math.rint(earthRadius * c / 1000.0);
    }
}
