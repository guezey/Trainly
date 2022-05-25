package com.fliers.trainly.models.trips;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Schedule Object 
 * @author Cengizhan Terzioğlu
 * @version 22.04.2021
*/
public class Schedule implements Serializable {
    
    // Properties
    private static Schedule tempInstance = null;
    private Calendar departureDate;
    private Calendar arrivalDate;
    private ArrayList<Wagon> wagons;
    private Train linkedTrain;
    private Line line;
    private double businessPrice;
    private double economyPrice;

    // Constructors

    /**
     * Creates a schedule for a train
     * @param departureDate departure date and time
     * @param arrivalDate arrival date and time
     * @param line which line the train is working
     * @param business business wagon number
     * @param economy economy wagon number
     * @param linkedTrain the train having this schedule
     */
    public Schedule( Calendar departureDate, Calendar arrivalDate, Line line, int business, int economy, Train linkedTrain) {
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.line = line;
        this.linkedTrain = linkedTrain;
        businessPrice = calculateBusinessPrice();
        economyPrice = calculateEconomyPrice();
        wagons = new ArrayList<>();
        createWagons( business, economy );
    }

    /**
     * Creates a schedule for a train
     * @param departureDateId id generated for the departure date and time
     * @param arrivalDateId id generate for the arrival date and time
     * @param line which line the train is working
     * @param business business wagon number
     * @param economy economy wagon number
     * @param linkedTrain the train having this schedule
     * @author Alp Afyonluoğlu
     */
    public Schedule( String departureDateId, String arrivalDateId, Line line, int business, int economy, Train linkedTrain) {
        this.departureDate = getDateFromIdRepresentation( departureDateId);
        this.arrivalDate = getDateFromIdRepresentation( arrivalDateId);
        this.line = line;
        this.linkedTrain = linkedTrain;
        wagons = new ArrayList<>();
        createWagons( business, economy );
    }

    // Methods

    /**
     * Getter method for departure date
     * @return departure date and time of the train
     */
    public Calendar getDepartureDate() {
        return this.departureDate;
    }

    /**
     * Getter method for arrival date
     * @return planned arrival date and time of the train
     */
    public Calendar getArrivalDate() {
        return this.arrivalDate;
    }

    /**
     * Getter method for wagons
     * @return an array list having the all wagons of the train
     */
    public ArrayList<Wagon> getWagons() {
        return this.wagons;
    }

    /**
     * Getter method for a specific wagon
     * @param wagonIndex index of the chosen wagon
     * @return the wagon of a train with the given index
     */
    public Wagon getWagon( int wagonIndex ) {
        return getWagons().get( wagonIndex );
    }

    /**
     * Getter method for linked train to a schedule
     * @return the train which has this schedule
     */
    public Train getLinkedTrain() {
        return this.linkedTrain;
    }

    /**
     * @return the departure place of the train
     */
    public Place getDeparturePlace() {
        return line.getDeparture();
    }

    /**
     * @return the arrival place
     */
    public Place getArrivalPlace() {
        return line.getArrival();
    }

    /**
     * Sets the new line
     * @param a
     */
    public void setLine( Line a) {
        line = a;
    }

    /**
     * Returns schedule's line.
     * @return line
     */
    public Line getLine() {
        return line;
    }

    private void createWagons( int business, int economy ) {
        Wagon currentWagon;

        for (int i = 0; i < business; i++) {
            currentWagon = new Wagon(this, true, i + 1);
            this.getWagons().add(currentWagon);
        }

        for (int j = 0; j < economy; j++) {
            currentWagon = new Wagon(this, false, business + j + 1);
            this.getWagons().add(currentWagon);
        }
    }

    /**
     * Calculates String version of given date to be used in ids
     * @return date as string
     * @author Alp Afyonluoğlu
     */
    public String getIdRepresentation( Calendar date) {
        // Variables
        String year;
        String month;
        String day;
        String hour;
        String minute;

        // Code
        year = String.valueOf( date.get( Calendar.YEAR));
        month = String.valueOf( date.get( Calendar.MONTH) + 1);
        day = String.valueOf( date.get( Calendar.DAY_OF_MONTH));
        hour = String.valueOf( date.get( Calendar.HOUR_OF_DAY));
        minute = String.valueOf( date.get( Calendar.MINUTE));

        if ( month.length() == 1) {
            month = "0" + month;
        }
        if ( day.length() == 1) {
            day = "0" + day;
        }
        if ( hour.length() == 1) {
            hour = "0" + hour;
        }
        if ( minute.length() == 1) {
            minute = "0" + minute;
        }

        return year + month + day + hour + minute;
    }

    /**
     * Calculates Date version of given id representation string
     * @param idRepresentation date as string
     * @return date as Date object
     * @author Alp Afyonluoğlu
     */
    public Calendar getDateFromIdRepresentation( String idRepresentation) {
        // Variables
        int year;
        int month;
        int day;
        int hour;
        int minute;
        Calendar date;

        // Code
        year = Integer.parseInt( idRepresentation.substring( 0, 4));
        month = Integer.parseInt( idRepresentation.substring( 4, 6));
        day = Integer.parseInt( idRepresentation.substring( 6, 8));
        hour = Integer.parseInt( idRepresentation.substring( 8, 10));
        minute = Integer.parseInt( idRepresentation.substring( 10, 12));

        date = Calendar.getInstance();
        date.set( year, month - 1, day, hour, minute);

        return date;
    }

    /**
     * Calculates and returns schedule's business ticket price.
     * @return business price
     * @author Ali Emir Güzey
     */
    private double calculateBusinessPrice() {
        BigDecimal bd = BigDecimal.valueOf(linkedTrain.getBusinessPrice() * line.getDistance() / 10);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Calculates and returns schedule's economy ticket price.
     * @return economy price
     * @author Ali Emir Güzey
     */
    private double calculateEconomyPrice() {
        BigDecimal bd = BigDecimal.valueOf(linkedTrain.getEconomyPrice() * line.getDistance() / 10);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Returns schedule's business ticket price.
     * @return business price
     * @author Ali Emir Güzey
     */
    public double getBusinessPrice() {
        return businessPrice;
    }

    /**
     * Returns schedule's economy ticket price.
     * @return economy price
     * @author Ali Emir Güzey
     */
    public double getEconomyPrice() {
        return economyPrice;
    }

    /**
     * Converts calendar to readble string
     * @param calendar calendar to be converted
     * @return converted string
     * @author Alp Afyonluoğlu
     */
    public static String calendarToString( Calendar calendar) {
        // Variables
        String year;
        String month;
        String day;
        String hour;
        String minute;

        // Code
        year = String.valueOf( calendar.get( Calendar.YEAR));
        month = String.valueOf( calendar.get( Calendar.MONTH) + 1);
        day = String.valueOf( calendar.get( Calendar.DAY_OF_MONTH));
        hour = String.valueOf( calendar.get( Calendar.HOUR_OF_DAY));
        minute = String.valueOf( calendar.get( Calendar.MINUTE));

        if ( month.length() == 1) {
            month = "0" + month;
        }
        if ( day.length() == 1) {
            day = "0" + day;
        }
        if ( hour.length() == 1) {
            hour = "0" + hour;
        }
        if ( minute.length() == 1) {
            minute = "0" + minute;
        }

        return day + "." + month + "." + year + " " + hour + ":" + minute;
    }

    /**
     * Getter method for static schedule temp instance
     * @return temp instance
     * @author Alp Afyonluoğlu
     */
    public static Schedule getTempInstance() {
        return tempInstance;

    }

    /**
     * Setter method for static schedule temp instance
     * @param schedule temp instance to be set
     * @author Alp Afyonluoğlu
     */
    public static void setTempInstance( Schedule schedule) {
        Schedule.tempInstance = schedule;
    }
}