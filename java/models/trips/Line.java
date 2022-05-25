package com.fliers.trainly.models.trips;

/**
 * Class that represents train lines.
 * @author Ali Emir Güzey
 * @version 02.05.2021
 */
public class Line {

    //properties
    private Place arrival;
    private Place departure;
    private int distance;

    //constructor

    /**
     * Creates a train line with departure and arrival info.
     * @param departure departure place
     * @param arrival arrival place
     */
    public Line( Place departure, Place arrival) {
        this.arrival = arrival;
        this.departure = departure;
        distance = calculateDistance();
    }

    //methods

    /**
     * Returns arrival place.
     * @return arrival
     */
    public Place getArrival() {
        return arrival;
    }

    /**
     * Returns departure place.
     * @return departure
     */
    public Place getDeparture() {
        return departure;
    }

    /**
     * Returns line's distance.
     * @return distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Returns String representation of the line.
     * @return String representation
     */
    @Override
    public String toString() {
        return departure.getName() + " - " + arrival.getName();
    }

    /**
     * Calculates line's distance.
     * @return line's distance
     */
    private int calculateDistance() {
        final int earthRadiusKm = 6371;
        double depLat;
        double arrLat;

        double diffLat = Math.toRadians(arrival.getLatitude() - departure.getLatitude());
        double diffLon = Math.toRadians(arrival.getLongitude() - departure.getLongitude());

        depLat = Math.toRadians(departure.getLatitude());
        arrLat = Math.toRadians(arrival.getLatitude());

        double a = Math.sin(diffLat/2) * Math.sin(diffLat/2) +
                Math.sin(diffLon/2) * Math.sin(diffLon/2) * Math.cos(depLat) * Math.cos(arrLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return (int) (earthRadiusKm * c);
    }
}
