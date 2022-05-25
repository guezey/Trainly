package com.fliers.trainly.models.trips;

/**
 * Interface giving x and y coordinate management to implemented classes
 * @author Alp Afyonluoğlu
 * @version 23.04.2021
 */
public interface Locatable {

    /**
     * Sets latitude and longitude
     * @param lat latitude
     * @param lon longitude
     */
    void setPos( double lat, double lon);

    /**
     * Getter method for latitude
     * @return latitude
     */
    double getLatitude();

    /**
     * Getter method for longitude
     * @return longitude
     */
    double getLongitude();
}
