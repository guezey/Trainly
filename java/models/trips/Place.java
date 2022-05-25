package com.fliers.trainly.models.trips;

/**
 * Class to represent places for train travels.
 * @author Ali Emir Güzey
 * @version 23.04.2021
 */
public class Place implements Locatable {

    //properties
    private String name;
    private double longitude;
    private double latitude;

    //constructor
    /**
     * Creates a Place object with name, latitude and longitude info.
     * @param name name of the place
     * @param lat latitude
     * @param lon longitude
     */
    public Place( String name, double lat, double lon) {
        longitude = lon;
        latitude = lat;
        this.name = name;
    }

    //methods
    /**
     * Returns the name of the place.
     * @return name of the place
     */
    public String getName() {
        return name;
    }

    /**
     * Returns latitude of the place.
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns longitude of the place.
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets a new position for the place.
     * @param lat new latitude
     * @param lon new longitude
     */
    public void setPos( double lat, double lon) {
        longitude = lon;
        latitude = lat;
    }
}
