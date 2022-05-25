package com.fliers.trainly.models.trips;

/**
* A class for seats in a Wagon
*@author Erkin Aydın
*@version 23-04-2021__v/1.1
*/
public class Seat {
    
    private String seatNumber;
    private Wagon linkedWagon;

    /**
    * The constructor of the Seat class
    * @param seatNumber
    * @param linkedWagon
    */
    public Seat( String seatNumber, Wagon linkedWagon) {
        this.seatNumber = seatNumber;
        this.linkedWagon = linkedWagon;
    }

    /**
    * @return linkedWagon
    */
    public Wagon getLinkedWagon() {
        return linkedWagon;
    }

    /**
    * @return seatNumber
    */
    public String getSeatNumber() {
        return seatNumber;
    }
}
