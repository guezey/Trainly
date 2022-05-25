package com.fliers.trainly.models.trips;

import com.fliers.trainly.models.users.Company;

import java.util.ArrayList;
import java.util.Calendar;

/**
* A class for trains. It will contain everything necessary for a Train, from its position to ticket prices;
 from its line to the Company it belongs.
* @author Erkin Aydın
* @version 23-04-2021__v/1.1
*/

public class Train {

    private static Train tempInstance = null;
    private double businessPrice;
    private double economyPrice;
    private String id;
    private int businessWagonNum;
    private int economyWagonNum;
    private ArrayList<Schedule> schedules;
    private Company linkedCompany;

    /**
    * Constructor of the Train class
    * @param company
    * @param businessWagonNum
    * @param economyWagonNum
    * @param bPrice
    * @param ePrice
    * @param id
     */
    public Train( Company company, int businessWagonNum,
                int economyWagonNum, double bPrice, double ePrice, String id) {
        linkedCompany = company;
        this.businessWagonNum = businessWagonNum;
        this.economyWagonNum = economyWagonNum;
        this.schedules = schedules;
        businessPrice = bPrice;
        economyPrice = ePrice;
        schedules = new ArrayList<>();
        this.id = id;
    }
    /**
    * @return id
    */
    public String getId() {
        return id;
    }
    /**
    * Sets the new business wagon number
    * @param a
    */
    public void setBusinessWagonNum( int a) {
        businessWagonNum = a;
    }

    /**
    * Sets the new economy wagon number
    * @param a
    */
    public void setEconomyWagonNum( int a) {
        economyWagonNum = a;
    }

    /**
    * Adds a new schedule
    * @param s
    */
    public void addSchedule( Schedule s) {
        schedules.add(s);
    }

    /**
    * @param d
    * @return the current schedule
    */
    public Schedule getSchedule( Calendar d) {
        for( Schedule s : schedules) {

            if(  s.getDepartureDate().compareTo( d) <= 0 && s.getArrivalDate().compareTo(d) >= 0) {
                return s;
            }
        }
        return null;
    }

    /**
    * @return the business class price
    */
    public double getBusinessPrice() {
        return businessPrice;
    }

    /**
    * @return the business price
    */
    public double getEconomyPrice() {
        return economyPrice;
    }

    /**
    * @return the company that the train belongs
    */
    public Company getLinkedCompany() {
        return linkedCompany;
    }

    /**
     * Getter method for business wagon number
     * @return business wagon number
     * @author Alp Afyonluoğlu
     */
    public int getBusinessWagonNum() {
        return businessWagonNum;
    }

    /**
     * Getter method for economy wagon number
     * @return economy wagon number
     * @author Alp Afyonluoğlu
     */
    public int getEconomyWagonNum() {
        return economyWagonNum;
    }

    /**
     * Returns whether the train is on trip.
     * @return true if train is on trip, false otherwise
     * @author Ali Emir Güzey
     */
    public boolean isOnTrip() {
        Calendar c;
        c = Calendar.getInstance();
        if (getSchedule(c) != null)
            return true;
        else
            return false;
    }

    /**
     * Returns schedules of the train
     * @return schedules
     * @author Ali Emir Güzey
     */
    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public static Train getTempInstance() {
        return tempInstance;

    }

    public static void setTempInstance( Train train) {
        Train.tempInstance = train;
    }
}
