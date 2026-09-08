package com.instana.robotshop.shipping;

/**
 * Bean to hold shipping information.
 */
public class Ship {

    private long distance;
    private double cost;

    public Ship() {
    }

    public Ship(long distance, double cost) {
        this.distance = distance;
        this.cost = cost;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getDistance() {
        return distance;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format("Distance: %d Cost: %.2f", distance, cost);
    }
}
