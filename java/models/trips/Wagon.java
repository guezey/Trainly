package com.fliers.trainly.models.trips;

import java.util.ArrayList;

/*
* A class for wagons. It will contain properties indicating prices for economy class and business class,
* an ArrayList for seats, a boolean property indicating wagons business condition and a Schedule
* @author Erkin Aydın
* @version 23-04-2021__v/1.1
*/

public class Wagon {
    private final String[] seatLetters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"};
    private final int BUSINESS_SEAT_NUM = 32;
    private final int ECONOMY_SEAT_NUM = 44;

    private ArrayList<Seat> seats;
    private boolean business;
    private Schedule linkedSchedule;
    private int wagonNumber;

    /**
    * The constructor of the Wagon class
    * @param schedule
    * @param b
    */
    public Wagon( Schedule schedule, boolean b, int wagonNumber) {
        this.wagonNumber = wagonNumber;
        linkedSchedule = schedule;
        business = b;
        createSeats();
    }

    /**
     * Creates empty seats
     * @author Erkin Aydın
     * @author Alp Afyonluoğlu
     */
    private void createSeats() {
        // Variables
        Seat seat;
        Wagon thisWagon;
        String seatNumber;
        String seatLetter;
        int totalSeatNo;

        // Code
        thisWagon = this;
        seats = new ArrayList<Seat>();
        if( isBusiness()) {
            totalSeatNo = BUSINESS_SEAT_NUM;
        }
        else {
            totalSeatNo = ECONOMY_SEAT_NUM;
        }

        for ( int count = 0; count < totalSeatNo; count++) {
            seatNumber = String.valueOf( ( count % 4) + 1);
            seatLetter = seatLetters[count / 4];
            seat = new Seat( seatLetter + seatNumber, thisWagon);
            seats.add( seat);
        }
    }

    /**
    * @return business
    */
    public boolean isBusiness() {
        return business;
    }

    /**
     * Getter method for the seat with given seat number
     * @param seatId number and letter of the seat
     * @return the specified seat
     * @author Erkin Aydın
     * @author Alp Afyonluoğlu
     */
    public Seat getSeat( String seatId) {
        // Variables
        String seatNumber;
        String seatLetter;
        int letterIndex;
        int seatIndex;

        // Code
        seatLetter = seatId.substring( 0, 1);
        seatNumber = seatId.substring( 1, 2);

        letterIndex = 0;
        for ( int count = 0; count < seatLetters.length; count++) {
            if ( seatLetters[count].equals( seatLetter)) {
                letterIndex = count;
                break;
            }
        }

        seatIndex = letterIndex * 4 + Integer.parseInt( seatNumber) - 1;
        return seats.get( seatIndex);
    }


    /**
     * Getter method for array list of seats
     * @return array list of seats
     */
    public ArrayList<Seat> getSeats() {
        return seats;
    }

    /**
    * @return total seat number depending on whether the wagon is business class or not
    */
    public int getTotalSeatNo() {
        if( isBusiness()) {
            return BUSINESS_SEAT_NUM;
        }
        else {
            return ECONOMY_SEAT_NUM;
        }
    }

    /**
    * @return linkedSchedule
    */
    public Schedule getLinkedSchedule() {
        return linkedSchedule;
    }

    /**
     * Getter method for wagon number
     * @return wagon number
     */
    public int getWagonNumber() {
        return wagonNumber;
    }
}
